package edu.usc.cs.mach.backend.service;

import edu.usc.cs.mach.backend.dto.AttributeSetting;
import edu.usc.cs.mach.backend.entity.ExcelFileWatchConfiguration;
import edu.usc.cs.mach.backend.entity.MachSyncJob;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelTransformService {

    public List<AttributeSetting> readExcelFile(ExcelFileWatchConfiguration configuration) throws IOException {
        String path = configuration.getFilePath();
        Path filePath = Paths.get(path);
        Resource resource = new UrlResource(filePath.toUri());
        List<AttributeSetting> settings = new ArrayList<>();
        if (resource.exists()) {

            XSSFWorkbook workbook = new XSSFWorkbook(resource.getInputStream());
            Long dataSheetNum = configuration.getDataSheetNum();
            XSSFSheet worksheet = workbook.getSheetAt(dataSheetNum.intValue());

            for(int i=0;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                AttributeSetting attributeSetting = new AttributeSetting();

                XSSFRow row = worksheet.getRow(i);
                //treat as string value
                attributeSetting.setCaptionName(row.getCell(0).toString());
                attributeSetting.setAttributeName(row.getCell(1).toString());

                XSSFCell formulaCell = row.getCell(2);

                if (formulaCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    try {
                        attributeSetting.setAttributeValue(formulaCell.getNumericCellValue() + "");
                    } catch (Exception e) {
                        attributeSetting.setAttributeValue(formulaCell.getStringCellValue());
                    }
                } else {
                    attributeSetting.setAttributeValue(formulaCell.toString());
                }

                settings.add(attributeSetting);
                System.out.printf("Row %d, read attr: %s, %s, %s\n", row.getRowNum(), attributeSetting.getCaptionName(), attributeSetting.getAttributeName(), attributeSetting.getAttributeValue());
            }
        }
        return settings;
    }

    public String transformToWindowsCSV(List<AttributeSetting> settings){
        StringBuilder sb = new StringBuilder();
        for(AttributeSetting setting: settings){
            sb.append(setting.asCSVLine());
            sb.append("\r\n");
        }
        return sb.toString();
    }

}

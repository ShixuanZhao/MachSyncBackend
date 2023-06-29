package edu.usc.cs.mach.backend.controller;

import edu.usc.cs.mach.backend.entity.ExcelFileWatchConfiguration;
import edu.usc.cs.mach.backend.service.ExcelFileWatchConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/config")
public class ConfigurationController {

    private final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
    private final ExcelFileWatchConfigurationService excelFileWatchConfigurationService;

    public ConfigurationController(ExcelFileWatchConfigurationService excelFileWatchConfigurationService) {
        this.excelFileWatchConfigurationService = excelFileWatchConfigurationService;
    }


    @GetMapping(path="/excel", produces = "application/json")
    public List<ExcelFileWatchConfiguration> excelConfig() {
        return this.excelFileWatchConfigurationService.listExcelConfiguration();
    }

    @PostMapping(path="/excel", produces = "application/json")
    public List<ExcelFileWatchConfiguration> addExcelConfig(@RequestBody ExcelFileWatchConfiguration excelFileWatchConfiguration){
        System.out.println(excelFileWatchConfiguration.getConfigurationName());
        this.excelFileWatchConfigurationService.addExcelConfiguration(excelFileWatchConfiguration);
        return this.excelFileWatchConfigurationService.listExcelConfiguration();
    }

    @PutMapping(path="/excel", produces = "application/json")
    public List<ExcelFileWatchConfiguration> updateExcelConfig(@RequestBody ExcelFileWatchConfiguration excelFileWatchConfiguration){
        System.out.println(excelFileWatchConfiguration.getConfigurationName());
        this.excelFileWatchConfigurationService.updateExcelConfiguration(excelFileWatchConfiguration);
        return this.excelFileWatchConfigurationService.listExcelConfiguration();
    }

    @PutMapping(path="/excel/{configurationName}", produces = "application/json")
    public List<ExcelFileWatchConfiguration> deleteExcelConfig(@PathVariable String configurationName){
        logger.info("Deleting configuration: " + configurationName);
        this.excelFileWatchConfigurationService.deleteExcelConfiguration(configurationName);
        return this.excelFileWatchConfigurationService.listExcelConfiguration();
    }

}

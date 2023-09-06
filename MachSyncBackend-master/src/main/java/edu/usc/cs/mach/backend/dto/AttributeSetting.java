package edu.usc.cs.mach.backend.dto;

import lombok.Data;
@Data
public class AttributeSetting implements CSVLineDataFormat {

    private String attributeName;
    private String attributeValue;
    private String captionName;

    @Override
    public String asCSVLine() {
        return captionName + "," + attributeName + "," + attributeValue;
    }
}

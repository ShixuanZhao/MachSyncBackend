package edu.usc.cs.mach.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ExcelFileWatchConfiguration {

    @Id
    private String configurationName;
    private String filePath;

    private Long dataSheetNum;

    private Boolean activated;

    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MachSyncJob> machSyncJobList;


}

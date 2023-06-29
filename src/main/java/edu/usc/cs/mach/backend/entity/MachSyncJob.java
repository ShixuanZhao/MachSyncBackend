package edu.usc.cs.mach.backend.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class MachSyncJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "configuration_name")
    private ExcelFileWatchConfiguration configuration;

    @Enumerated(EnumType.STRING)
    private MachSyncJobType job_type;


    @Enumerated(EnumType.STRING)
    private MachJobStatus status;


    private String created_time;
    private String completed_time;
}

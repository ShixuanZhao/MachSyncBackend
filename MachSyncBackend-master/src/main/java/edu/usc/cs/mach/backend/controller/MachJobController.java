package edu.usc.cs.mach.backend.controller;

import edu.usc.cs.mach.backend.dto.AttributeSetting;
import edu.usc.cs.mach.backend.entity.ExcelFileWatchConfiguration;
import edu.usc.cs.mach.backend.entity.MachJobStatus;
import edu.usc.cs.mach.backend.entity.MachSyncJob;
import edu.usc.cs.mach.backend.service.ExcelFileWatchConfigurationService;
import edu.usc.cs.mach.backend.service.ExcelTransformService;
import edu.usc.cs.mach.backend.service.MachJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/job")
public class MachJobController {

    private final Logger logger = LoggerFactory.getLogger(MachJobController.class);
    private final MachJobService machJobService;
    private final ExcelFileWatchConfigurationService excelFileWatchConfigurationService;
    private final ExcelTransformService excelTransformService;

    public MachJobController(MachJobService machJobService,
                             ExcelFileWatchConfigurationService excelFileWatchConfigurationService,
                             ExcelTransformService excelTransformService) {
        this.machJobService = machJobService;
        this.excelFileWatchConfigurationService = excelFileWatchConfigurationService;
        this.excelTransformService = excelTransformService;
    }

    @GetMapping(path="", produces = "application/json")
    public List<MachSyncJob> getJobs(){
        return machJobService.listMachineJobs();
    }

    @PutMapping(path="", produces = "application/json")
    public void updateJob(MachSyncJob machSyncJob){
        machJobService.updateMachJob(machSyncJob);
    }

    @GetMapping(path = "/{id}/csv")
    public String getJob(@PathVariable Long id)throws Exception{
        Optional<MachSyncJob> machJob = this.machJobService.getMachJob(id);
        if(machJob.isPresent()){
            MachSyncJob machSyncJob = machJob.get();
            ExcelFileWatchConfiguration excelConfiguration = machSyncJob.getConfiguration();


            List<AttributeSetting> settings = excelTransformService.readExcelFile(excelConfiguration);
            return excelTransformService.transformToWindowsCSV(settings);

        }
        return "";
    }

    @GetMapping("next")
    public Long getNextAvailableJob(){
        logger.info("fetch job");
        Optional<MachSyncJob> nextJob = machJobService.getNextAvailableJob();
        if(nextJob.isPresent()){
            Long id = nextJob.get().getId();
            nextJob.get().setStatus(MachJobStatus.FETCHED);
            machJobService.updateMachJob(nextJob.get());
            logger.info("fetch job returns {}", id);
            return id;

        }
        return -1L;
    }

    //we split the status because debugging in the machinations is a bit of hard
    @GetMapping("/{id}/success")
    public void successJob(@PathVariable Long id){
        logger.info("success job {}", id);
        Optional<MachSyncJob> finishedJob = machJobService.getMachJob(id);
        if(finishedJob.isPresent()){
            finishedJob.get().setStatus(MachJobStatus.SUCCESS);
            finishedJob.get().setCompleted_time(new Date().toString());
            machJobService.updateMachJob(finishedJob.get());
            return;

        }
        return;
    }

    @GetMapping("/{id}/failed")
    public void failJob(@PathVariable Long id){

        Optional<MachSyncJob> finishedJob = machJobService.getMachJob(id);
        if(finishedJob.isPresent()){
            finishedJob.get().setStatus(MachJobStatus.FAIL);
            machJobService.updateMachJob(finishedJob.get());
            return;
        }
        return;
    }


}

package edu.usc.cs.mach.backend.schedule;

import edu.usc.cs.mach.backend.entity.ExcelFileWatchConfiguration;
import edu.usc.cs.mach.backend.entity.MachJobStatus;
import edu.usc.cs.mach.backend.entity.MachSyncJob;
import edu.usc.cs.mach.backend.entity.MachSyncJobType;
import edu.usc.cs.mach.backend.respository.ExcelFileWatchConfigurationRepository;
import edu.usc.cs.mach.backend.respository.MachJobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WatchExcelJob {

    private final ExcelFileWatchConfigurationRepository excelFileWatchConfigurationRepository;
    private final MachJobRepository machJobRepository;

    private final Map<String, Date> fileModifyMap = new ConcurrentHashMap<>();

    public WatchExcelJob(ExcelFileWatchConfigurationRepository excelFileWatchConfigurationRepository, MachJobRepository machJobRepository){
        this.excelFileWatchConfigurationRepository = excelFileWatchConfigurationRepository;
        this.machJobRepository = machJobRepository;
    }

    private boolean shouldRunJobByDate(Date modificationDate, String path){
        return !fileModifyMap.containsKey(path) || (fileModifyMap.containsKey(path) && fileModifyMap.get(path).before(modificationDate));
    }

    private void mergeJobs(){
        List<MachSyncJob> all = this.machJobRepository.findAll();
        all.sort(Comparator.comparing(MachSyncJob::getCreated_time).reversed());
        Map<String, MachSyncJob> dedupeMap = new ConcurrentHashMap<String, MachSyncJob>();
        for(MachSyncJob job: all){
            // we use configuration name as key instead of file path
            // because we might later implement other requirements like a job monitor multiple files
            String key = job.getConfiguration().getConfigurationName();
            if(dedupeMap.containsKey(key)){
                // we could cancel it because we have a job later on
                if(job.getStatus().equals(MachJobStatus.PENDING)){
                    job.setStatus(MachJobStatus.CANCELLED);
                    machJobRepository.save(job);
                }
            } else {
                dedupeMap.put(key, job);
            }
        }
    }

    private void updateJobsList() throws IOException, URISyntaxException {
        List<ExcelFileWatchConfiguration> all = this.excelFileWatchConfigurationRepository.findAll();
        for(ExcelFileWatchConfiguration excelFileWatchConfiguration : all){
            if(excelFileWatchConfiguration.getActivated()){
                String path = excelFileWatchConfiguration.getFilePath();
                String fileProtocol = "file:///"+path;
                Path filePath = Paths.get(new URL(fileProtocol).toURI());
                if(filePath.toFile().exists()){
                    BasicFileAttributes fileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                    Date modificationDate = new Date(fileAttributes.lastModifiedTime().toMillis());

                    MachSyncJob machSyncJob = new MachSyncJob();
                    machSyncJob.setJob_type(MachSyncJobType.EXCEL_FILE_WATCH_JOB);
                    machSyncJob.setStatus(MachJobStatus.PENDING);
                    machSyncJob.setCreated_time(new Date().toString());
                    machSyncJob.setCompleted_time(null);
                    machSyncJob.setConfiguration(excelFileWatchConfiguration);
                    if(shouldRunJobByDate(modificationDate, path)){
                        fileModifyMap.put(path, modificationDate);
                        machJobRepository.save(machSyncJob);
                    }
                }

            }
        }
        mergeJobs();
    }

   @Scheduled(fixedRateString = "${job.fixedRate}")
    public void runJob() throws IOException, URISyntaxException {
       updateJobsList();
    }
}

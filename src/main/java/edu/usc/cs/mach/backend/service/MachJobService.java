package edu.usc.cs.mach.backend.service;

import edu.usc.cs.mach.backend.entity.MachJobStatus;
import edu.usc.cs.mach.backend.entity.MachSyncJob;
import edu.usc.cs.mach.backend.respository.MachJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MachJobService {
    private final MachJobRepository machJobRepository;

    public MachJobService(@Autowired MachJobRepository machJobRepository) {
        this.machJobRepository = machJobRepository;
    }

    public void addMachJob(MachSyncJob machSyncJob) {
        machJobRepository.save(machSyncJob);
    }

    public void updateMachJob(MachSyncJob machSyncJob) {
        machJobRepository.save(machSyncJob);
    }

    public void deleteMachJob(MachSyncJob machSyncJob) {
        machJobRepository.delete(machSyncJob);
    }

    public List<MachSyncJob> listMachineJobs() {
        return machJobRepository.findAll();
    }

    public Optional<MachSyncJob> getMachJob(Long id) {
        return machJobRepository.findById(id);
    }

    public Optional<MachSyncJob> getNextAvailableJob(){
        return machJobRepository.findAll().stream().filter(job -> job.getStatus().equals(MachJobStatus.PENDING)).findFirst();
    }
}

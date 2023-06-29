package edu.usc.cs.mach.backend.respository;

import edu.usc.cs.mach.backend.entity.MachSyncJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachJobRepository extends JpaRepository<MachSyncJob, Long> {


}

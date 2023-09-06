package edu.usc.cs.mach.backend.respository;

import edu.usc.cs.mach.backend.entity.ExcelFileWatchConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExcelFileWatchConfigurationRepository extends JpaRepository<ExcelFileWatchConfiguration, String>{
    Optional<ExcelFileWatchConfiguration> findByConfigurationName(String configurationName);

}

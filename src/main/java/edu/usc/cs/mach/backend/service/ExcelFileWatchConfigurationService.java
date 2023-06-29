package edu.usc.cs.mach.backend.service;

import edu.usc.cs.mach.backend.entity.ExcelFileWatchConfiguration;
import edu.usc.cs.mach.backend.respository.ExcelFileWatchConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExcelFileWatchConfigurationService {

    private final ExcelFileWatchConfigurationRepository excelFileWatchConfigurationRepository;

    @Autowired
    public ExcelFileWatchConfigurationService(ExcelFileWatchConfigurationRepository excelFileWatchConfigurationRepository) {
        this.excelFileWatchConfigurationRepository = excelFileWatchConfigurationRepository;
    }

    public List<ExcelFileWatchConfiguration> listExcelConfiguration() {
        return excelFileWatchConfigurationRepository.findAll();
    }

    public void addExcelConfiguration(ExcelFileWatchConfiguration excelFileWatchConfiguration) {
        excelFileWatchConfigurationRepository.save(excelFileWatchConfiguration);
    }

    public Optional<ExcelFileWatchConfiguration> getExcelConfiguration(String configurationName) {
        return excelFileWatchConfigurationRepository.findByConfigurationName(configurationName);
    }

    public void updateExcelConfiguration(ExcelFileWatchConfiguration excelFileWatchConfiguration) {
        excelFileWatchConfigurationRepository.save(excelFileWatchConfiguration);
    }

    public void deleteExcelConfiguration(String id) {
        excelFileWatchConfigurationRepository.deleteById(id);
    }
}

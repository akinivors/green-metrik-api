package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.ActivityLogResponse;
import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import com.greenmetrik.greenmetrikapi.repository.ActivityLogRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ActivityLogRepository activityLogRepository;

    public ReportService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Transactional(readOnly = true)
    public byte[] exportActivityLogReport() throws FileNotFoundException, JRException {
        // Fetch data directly from repository with proper transaction context
        List<ActivityLog> activityLogs = activityLogRepository.findAll();

        // Convert to DTOs within the transaction context
        List<ActivityLogResponse> activityLogResponses = activityLogs.stream()
                .map(ActivityLogResponse::fromEntity)
                .collect(Collectors.toList());

        // Debug: Check if we have data
        System.out.println("DEBUG: Activity logs count: " + activityLogResponses.size());
        if (activityLogResponses.isEmpty()) {
            System.out.println("WARNING: No activity logs found for report generation");
        }

        File file = ResourceUtils.getFile("classpath:reports/activity_log_report.jrxml");
        System.out.println("DEBUG: Template file path: " + file.getAbsolutePath());

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(activityLogResponses);
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}

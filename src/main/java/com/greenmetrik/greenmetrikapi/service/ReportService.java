package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.model.ActivityLog;
import com.greenmetrik.greenmetrikapi.repository.ActivityLogRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final ActivityLogRepository activityLogRepository;

    public ReportService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public byte[] exportActivityLogReport() throws FileNotFoundException, JRException {
        // 1. Fetch all activity logs (we want the full report, not a paginated one)
        List<ActivityLog> activityLogs = activityLogRepository.findAll();

        // 2. Load the report template file (.jrxml)
        File file = ResourceUtils.getFile("classpath:reports/activity_log_report.jrxml");

        // 3. Compile the report
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // 4. Create a JRBeanCollectionDataSource to provide the data to the report
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(activityLogs);

        // 5. Create a map for any report parameters (we don't have any for this simple report)
        Map<String, Object> parameters = new HashMap<>();

        // 6. Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 7. Export the report to a byte array (PDF format)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}

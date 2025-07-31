package com.greenmetrik.greenmetrikapi.controller;

import com.greenmetrik.greenmetrikapi.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/activity-log")
    @PreAuthorize("hasRole('ADMIN')")
    public void generateActivityLogReport(HttpServletResponse response) throws IOException, JRException {
        // Set the content type and headers for the response
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=activity_log_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Call the service to get the report content
        byte[] reportContent = reportService.exportActivityLogReport();

        // Write the report content to the response's output stream
        response.getOutputStream().write(reportContent);
    }
}

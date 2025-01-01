package com.security.duanspringboot.controller;

import com.security.duanspringboot.service.report.RestockProductCategoryReport;
import com.security.duanspringboot.service.report.TopSellingProductReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/un_auth")
@Slf4j
@RequiredArgsConstructor
public class ReportController {
    private final TopSellingProductReport topSellingProductReport;
    private final RestockProductCategoryReport restockProductCategoryReport;

    @GetMapping("/report/top-selling-products")
    public ResponseEntity<byte[]> generateTopSellingProductReport(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String categoryId) {
        Map<String, String> reportContent = topSellingProductReport.generateReport(startDate, endDate, categoryId);

        String reportName = reportContent.keySet().iterator().next();
        String csvContent = reportContent.get(reportName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + reportName);
        headers.add("Content-Type", "text/csv; charset=UTF-8");

        return new ResponseEntity<>(csvContent.getBytes(), headers, HttpStatus.OK);
    }

    @GetMapping("/report/restock-product-category")
    public ResponseEntity<byte[]> generateRestockProductCategoryReport(@RequestParam String startDate, @RequestParam String categoryId) {
        Map<String, String> reportContent = restockProductCategoryReport.generateReport(startDate, categoryId);

        String reportName = reportContent.keySet().iterator().next();
        String csvContent = reportContent.get(reportName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + reportName);
        headers.add("Content-Type", "text/csv; charset=UTF-8");

        return new ResponseEntity<>(csvContent.getBytes(), headers, HttpStatus.OK);
    }
}

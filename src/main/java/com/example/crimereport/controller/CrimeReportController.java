package com.example.crimereport.controller;

import com.example.crimereport.model.entity.CrimeReport;
import com.example.crimereport.service.CrimeReportService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class CrimeReportController {

    private final CrimeReportService crimeReportService;

    public CrimeReportController(CrimeReportService crimeReportService) {
        this.crimeReportService = crimeReportService;
    }

    @GetMapping
    public List<CrimeReport> getReports(@RequestParam(required = false) String states, @RequestParam(required = false) String years, @RequestParam(required = false) String crimes) {
        List<Long> inputStates = states != null ? Arrays.stream(states.split(",")).map(Long::parseLong).toList() : null;
        List<Long> inputYears = years != null ? Arrays.stream(years.split(",")).map(Long::parseLong).toList() : null;
        List<Long> inputCrimes = crimes != null ? Arrays.stream(crimes.split(",")).map(Long::parseLong).toList() : null;
        return crimeReportService.getDataFromQuery(inputStates, inputYears, inputCrimes);
    }

}

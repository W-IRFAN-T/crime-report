package com.example.crimereport.service;

import com.example.crimereport.model.entity.CrimeReport;
import com.example.crimereport.repo.CrimeReportRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrimeReportService {

    private final CrimeReportRepository crimeReportRepository;

    public CrimeReportService(CrimeReportRepository crimeReportRepository) {
        this.crimeReportRepository = crimeReportRepository;
    }

    public List<CrimeReport> getDataFromQuery(List<Long> states, List<Long> years, List<Long> crimes) {

        boolean getStates = states != null && !states.isEmpty();
        boolean getYears = years != null && !years.isEmpty();
        boolean getCrimes = crimes != null && !crimes.isEmpty();

        if(getStates && getYears && getCrimes) {
            return crimeReportRepository.getReportsForStatesAndYearsAndCases(states, years, crimes);
        } else if (getStates && getYears) {
            return crimeReportRepository.getReportsForStatesAndYears(states, years);
        } else if (getStates && getCrimes) {
            return crimeReportRepository.getReportsForStatesAndCases(states, crimes);
        } else if (getYears && getCrimes) {
            return crimeReportRepository.getReportsForCasesAndYears(years, crimes);
        } else if (getStates) {
            return crimeReportRepository.getReportsForStates(states);
        } else if (getYears) {
            return crimeReportRepository.getReportsForYears(years);
        } else if (getCrimes) {
            return crimeReportRepository.getReportsForCases(crimes);
        } else {
            return crimeReportRepository.findAll();
        }

    }

}

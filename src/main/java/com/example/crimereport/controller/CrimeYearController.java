package com.example.crimereport.controller;

import com.example.crimereport.model.entity.CrimeYear;
import com.example.crimereport.repo.CrimeYearRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/years")
public class CrimeYearController {

    private final CrimeYearRepository crimeYearRepository;

    public CrimeYearController(CrimeYearRepository crimeYearRepository) {
        this.crimeYearRepository = crimeYearRepository;
    }

    @GetMapping
    public List<CrimeYear> getYears() {
        return crimeYearRepository.findAll();
    }

    @GetMapping("{id}")
    public CrimeYear getYear(@PathVariable("id") Long yearId) {
        return crimeYearRepository.findById(yearId).orElseThrow(() -> new RuntimeException("Exception while fetching year with ID : {0}" + yearId));
    }
}

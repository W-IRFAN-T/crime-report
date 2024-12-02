package com.example.crimereport.controller;

import com.example.crimereport.model.entity.Crime;
import com.example.crimereport.repo.CrimeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crimes")
public class CrimeController {

    private final CrimeRepository crimeRepository;

    public CrimeController(CrimeRepository crimeRepository) {
        this.crimeRepository = crimeRepository;
    }

    @GetMapping
    public List<Crime> getCrimes() {
        return crimeRepository.findAll();
    }

    @GetMapping("{id}")
    public Crime getCrime(@PathVariable("id") Long crimeId) {
        return crimeRepository.findById(crimeId).orElseThrow(() -> new RuntimeException("Exception while fetching crime with ID : {0}" + crimeId));
    }

}

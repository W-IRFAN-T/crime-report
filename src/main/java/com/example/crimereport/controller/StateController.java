package com.example.crimereport.controller;

import com.example.crimereport.model.entity.State;
import com.example.crimereport.repo.StateRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/states")
public class StateController {

    private final StateRepository stateRepository;

    public StateController(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @GetMapping
    public List<State> getStates() {
        return stateRepository.findAll();
    }

    @GetMapping("{id}")
    public State getState(@PathVariable("id") Long stateId) {
        return stateRepository.findById(stateId).orElseThrow(() -> new RuntimeException("Exception while fetching state with ID : {0}" + stateId));
    }

}

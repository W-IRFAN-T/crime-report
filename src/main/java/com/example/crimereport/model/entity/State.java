package com.example.crimereport.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "STATE")
public class State {
    @Id
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "STATE_NAME")
    private String stateName;
}

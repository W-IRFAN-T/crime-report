package com.example.crimereport.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CRIME")
public class Crime {
    @Id
    @Column(name = "CRIME_ID")
    private Long crimeId;
    @Column(name = "CRIME_NAME")
    private String crimeName;
}

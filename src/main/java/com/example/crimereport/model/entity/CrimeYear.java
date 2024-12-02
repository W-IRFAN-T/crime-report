package com.example.crimereport.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CRIME_YEAR")
public class CrimeYear {
    @Id
    @Column(name = "YEAR_ID")
    private Long yearId;
    @Column(name = "YEAR_NAME")
    private String yearName;
}

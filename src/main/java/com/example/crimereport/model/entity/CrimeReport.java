package com.example.crimereport.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CRIME_REPORT")
public class CrimeReport {
    @Id
    @Column(name="REPORT_ID")
    private Long reportId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="STATE_ID", referencedColumnName = "STATE_ID")
    private State state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="YEAR_ID", referencedColumnName = "YEAR_ID")
    private CrimeYear crimeYear;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CRIME_ID", referencedColumnName = "CRIME_ID")
    private Crime crime;

    @Column(name="COUNT")
    private Long count;
}

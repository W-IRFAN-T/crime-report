package com.example.crimereport.repo;

import com.example.crimereport.model.entity.CrimeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrimeReportRepository extends JpaRepository<CrimeReport, Long> {

    @Query("select cr from CrimeReport cr where cr.state.stateId in (:states) and cr.crimeYear.yearId in (:years) and cr.crime.crimeId in (:crimes)")
    List<CrimeReport> getReportsForStatesAndYearsAndCases(@Param("states") List<Long> states, @Param("years") List<Long> years, @Param("crimes") List<Long> crimes);

    @Query("select cr from CrimeReport cr where cr.state.stateId in (:states)")
    List<CrimeReport> getReportsForStates(@Param("states") List<Long> states);

    @Query("select cr from CrimeReport cr where cr.crimeYear.yearId in (:years)")
    List<CrimeReport> getReportsForYears(@Param("years") List<Long> years);

    @Query("select cr from CrimeReport cr where cr.crime.crimeId in (:crimes)")
    List<CrimeReport> getReportsForCases(@Param("crimes") List<Long> crimes);

    @Query("select cr from CrimeReport cr where cr.state.stateId in (:states) and cr.crimeYear.yearId in (:years)")
    List<CrimeReport> getReportsForStatesAndYears(@Param("states") List<Long> states, @Param("years") List<Long> years);

    @Query("select cr from CrimeReport cr where cr.state.stateId in (:states) and cr.crime.crimeId in (:crimes)")
    List<CrimeReport> getReportsForStatesAndCases(@Param("states") List<Long> states, @Param("crimes") List<Long> crimes);

    @Query("select cr from CrimeReport cr where cr.crimeYear.yearId in (:years) and cr.crime.crimeId in (:crimes)")
    List<CrimeReport> getReportsForCasesAndYears(@Param("years") List<Long> years, @Param("crimes") List<Long> crimes);

}

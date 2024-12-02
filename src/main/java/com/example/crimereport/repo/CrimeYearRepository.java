package com.example.crimereport.repo;

import com.example.crimereport.model.entity.CrimeYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrimeYearRepository extends JpaRepository<CrimeYear, Long> {}

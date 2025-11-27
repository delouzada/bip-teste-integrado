package com.example.repository;

import com.example.ejb.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {

    List<Beneficio> findByAtivoTrue();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Beneficio> findWithLockingById(Long id);
}

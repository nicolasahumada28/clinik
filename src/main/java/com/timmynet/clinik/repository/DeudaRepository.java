package com.timmynet.clinik.repository;

import com.timmynet.clinik.domain.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeudaRepository extends JpaRepository<Deuda, Long> {
    List<Deuda> findByPacienteId(Long pacienteId);
}

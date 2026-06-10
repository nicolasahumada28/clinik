package com.timmynet.clinik.repository;

import com.timmynet.clinik.domain.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByProfesionalId(Long profesionalId);
}

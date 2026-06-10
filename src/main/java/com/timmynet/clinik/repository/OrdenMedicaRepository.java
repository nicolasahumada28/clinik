package com.timmynet.clinik.repository;

import com.timmynet.clinik.domain.OrdenMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenMedicaRepository extends JpaRepository<OrdenMedica, Long> {
    List<OrdenMedica> findByPacienteId(Long pacienteId);
}

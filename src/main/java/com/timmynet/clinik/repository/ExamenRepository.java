package com.timmynet.clinik.repository;

import com.timmynet.clinik.domain.Examen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamenRepository extends JpaRepository<Examen, Long> {
}

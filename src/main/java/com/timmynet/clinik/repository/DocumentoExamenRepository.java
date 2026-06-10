package com.timmynet.clinik.repository;

import com.timmynet.clinik.domain.DocumentoExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoExamenRepository extends JpaRepository<DocumentoExamen, Long> {
    List<DocumentoExamen> findByExamenId(Long examenId);
}

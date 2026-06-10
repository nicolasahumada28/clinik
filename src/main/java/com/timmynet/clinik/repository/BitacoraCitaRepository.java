package com.timmynet.clinik.repository;

import com.timmynet.clinik.domain.BitacoraCita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BitacoraCitaRepository extends JpaRepository<BitacoraCita, Long> {
    List<BitacoraCita> findByCitaId(Long citaId);
}

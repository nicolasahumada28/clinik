package com.timmynet.clinik.repository;

import com.timmynet.clinik.domain.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByDeudaId(Long deudaId);
}

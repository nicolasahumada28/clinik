package com.timmynet.clinik.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponse(Long id, Long deudaId, BigDecimal monto, LocalDateTime fechaPago,
                           String metodoPago, LocalDateTime createdAt, LocalDateTime updatedAt) {}

package com.timmynet.clinik.dto;

import java.math.BigDecimal;

public record PagoRequest(Long deudaId, BigDecimal monto, String metodoPago) {}

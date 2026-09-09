package com.timmynet.clinik.dto;

import java.math.BigDecimal;

public record DeudaRequest(Long pacienteId, BigDecimal montoTotal, BigDecimal balance) {}

package com.timmynet.clinik.dto;

import java.time.LocalDate;

public record PacienteResponse(
    Long id,
    String numeroDocumento,
    String nombre,
    String apellido,
    LocalDate fechaNacimiento,
    String telefono,
    String email,
    String direccion,
    LocalDate createdAt,
    LocalDate updatedAt
) {}

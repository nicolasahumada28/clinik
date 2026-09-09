package com.timmynet.clinik.dto;

import java.time.LocalDate;

public record PacienteRequest(
    String numeroDocumento,
    String nombre,
    String apellido,
    LocalDate fechaNacimiento,
    String telefono,
    String email,
    String direccion
) {}

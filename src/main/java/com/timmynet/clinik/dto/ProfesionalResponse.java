package com.timmynet.clinik.dto;

public record ProfesionalResponse(
    Long id,
    String nombre,
    String apellido,
    String especialidad,
    String numeroLicencia,
    String telefono,
    String email
) {}

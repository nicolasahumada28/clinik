package com.timmynet.clinik.dto;

public record ProfesionalRequest(
    String nombre,
    String apellido,
    String especialidad,
    String numeroLicencia,
    String telefono,
    String email
) {}

package com.timmynet.clinik.dto;

import com.timmynet.clinik.domain.*;
import java.util.List;

public final class DtoMapper {

    private DtoMapper() {}

    public static PacienteResponse toResponse(Paciente value) {
        return new PacienteResponse(value.getId(), value.getNumeroDocumento(), value.getNombre(), value.getApellido(),
            value.getFechaNacimiento(), value.getTelefono(), value.getEmail(), value.getDireccion(),
            value.getCreatedAt(), value.getUpdatedAt());
    }

    public static ProfesionalResponse toResponse(Profesional value) {
        return new ProfesionalResponse(value.getId(), value.getNombre(), value.getApellido(), value.getEspecialidad(),
            value.getNumeroLicencia(), value.getTelefono(), value.getEmail());
    }

    public static RolResponse toResponse(Rol value) {
        return new RolResponse(value.getId(), value.getNombre(), value.getActivo(), value.getCreatedAt(), value.getUpdatedAt());
    }

    public static UsuarioResponse toResponse(Usuario value) {
        return new UsuarioResponse(value.getId(), value.getUsername(), value.getEmail(), idOf(value.getRol()));
    }

    public static CitaResponse toResponse(Cita value) {
        List<BitacoraResponse> logs = value.getLogs() == null ? List.of() : value.getLogs().stream().map(DtoMapper::toResponse).toList();
        return new CitaResponse(value.getId(), value.getTipoCita(), idOf(value.getPaciente()), idOf(value.getProfesional()),
            value.getScheduledAt(), value.getEstadoCita(), value.getAsistio(), value.getRazonCancelacion(), value.getNotas(),
            value.getCreatedAt(), value.getUpdatedAt(), logs);
    }

    public static BitacoraResponse toResponse(BitacoraCita value) {
        return new BitacoraResponse(value.getId(), idOf(value.getCita()), value.getNote(), value.getCreatedAt(), value.getUpdatedAt());
    }

    public static DeudaResponse toResponse(Deuda value) {
        List<PagoResponse> payments = value.getPayments() == null ? List.of() : value.getPayments().stream().map(DtoMapper::toResponse).toList();
        return new DeudaResponse(value.getId(), idOf(value.getPaciente()), value.getMontoTotal(), value.getBalance(),
            value.getCreatedAt(), value.getUpdatedAt(), value.getEstado(), payments);
    }

    public static PagoResponse toResponse(Pago value) {
        return new PagoResponse(value.getId(), idOf(value.getDeuda()), value.getMonto(), value.getFechaPago(),
            value.getMetodoPago(), value.getCreatedAt(), value.getUpdatedAt());
    }

    public static ExamenResponse toResponse(Examen value) {
        List<DocumentoResponse> documents = value.getDocumentos() == null ? List.of() : value.getDocumentos().stream().map(DtoMapper::toResponse).toList();
        return new ExamenResponse(value.getId(), value.getNombre(), value.getDescripcion(), value.getCreatedAt(),
            value.getUpdatedAt(), idOf(value.getCita()), documents);
    }

    public static DocumentoResponse toResponse(DocumentoExamen value) {
        return new DocumentoResponse(value.getId(), value.getFilename(), value.getContentType(), value.getFileSize(),
            value.getUploadedAt(), value.getUpdatedAt(), idOf(value.getExamen()));
    }

    public static OrdenMedicaResponse toResponse(OrdenMedica value) {
        return new OrdenMedicaResponse(value.getId(), value.getTipoOrdenMedica(), idOf(value.getPaciente()),
            idOf(value.getProfesional()), idOf(value.getCita()), value.getDescripcion(), value.getCreatedAt(), value.getUpdatedAt());
    }

    private static Long idOf(Object value) {
        if (value instanceof Paciente entity) return entity.getId();
        if (value instanceof Profesional entity) return entity.getId();
        if (value instanceof Rol entity) return entity.getId();
        if (value instanceof Cita entity) return entity.getId();
        if (value instanceof Deuda entity) return entity.getId();
        if (value instanceof Examen entity) return entity.getId();
        return null;
    }
}

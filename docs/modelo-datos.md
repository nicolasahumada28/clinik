# Modelo de datos

Entidades actuales: `Usuario`, `Rol`, `Paciente`, `Profesional`, `Cita`, `BitacoraCita`, `OrdenMedica`, `Examen`, `DocumentoExamen`, `Deuda` y `Pago`.

Las respuestas REST usan DTOs y referencias por ID para evitar ciclos JPA. Los bytes de documentos no se serializan en respuestas; se exponen sus metadatos.

La historia clinica, especialidades, sucursales, atenciones, odontograma, inventario y auditoria son extensiones del roadmap.

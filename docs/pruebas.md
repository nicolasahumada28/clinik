# Casos de prueba

## 1. Suite actual

| Clase | Tipo | Casos |
|---|---|---:|
| `ClinikApplicationTests` | Integracion de contexto | 1 |
| `CitaControllerTest` | Unitaria con Mockito | 5 |

Ejecucion:

```powershell
.\mvnw.cmd test
```

## 2. Casos implementados para citas

### Listado

`shouldReturnAllCitas` verifica respuesta HTTP 200, cantidad de resultados y mapeo de `TipoCita` al DTO.

### Creacion

`shouldScheduleCita` verifica que se resuelvan paciente y profesional, que la cita se guarde y que responda HTTP 201 con estado `PROGRAMADA` por defecto.

### Asistencia

`shouldRegisterAttendance` verifica que asistencia positiva cambie el estado a `COMPLETADA`.

### Referencia inexistente

`shouldRejectScheduleWhenPacienteDoesNotExist` verifica HTTP 404 y que no se persista una cita cuando el paciente no existe.

### Cancelacion

`shouldCancelScheduledCitaWithReason` verifica estado `CANCELADA`, limpieza del motivo con `trim()` y fecha de actualizacion.

## 3. Validacion de contexto

`ClinikApplicationTests.contextLoads` levanta Spring Boot con H2, JPA, seguridad JWT, repositorios, inicializador y configuracion OpenAPI.

## 4. Cobertura recomendada

Como siguiente etapa se recomienda agregar:

- Login correcto, password invalida y token expirado.
- Acceso sin Bearer y acceso con Bearer valido.
- CRUD y validaciones de pacientes/profesionales.
- Cancelacion de cita completada o con ausencia, esperando HTTP 409.
- Subida, listado y descarga de documentos.
- Pagos que exceden balance y estados de deuda.
- Pruebas de serializacion DTO sin password ni bytes binarios.
- Pruebas de integracion con PostgreSQL mediante Testcontainers.

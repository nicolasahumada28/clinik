# Catalogo de clases y estructuras

## 1. Paquetes

| Paquete | Responsabilidad |
|---|---|
| `domain` | Entidades JPA y enumeraciones del dominio clinico. |
| `dto` | Contratos de entrada y salida de la API. |
| `controller` | Endpoints REST y codigos HTTP. |
| `repository` | Acceso a datos mediante Spring Data JPA. |
| `security` | Autenticacion JWT, usuarios Spring y filtro Bearer. |
| `config` | Inicializacion de datos y configuracion OpenAPI. |

La implementacion actual no contiene aun una capa `service` independiente: los controladores coordinan repositorios, conversion DTO y reglas sencillas del MVP.

## 2. Entidades y enums

### `Paciente`

Representa a la persona atendida. Contiene `id`, numero de documento, nombre, apellido, fecha de nacimiento, telefono, email, direccion y marcas de auditoria temporal. Se relaciona con citas y deudas.

### `Profesional`

Representa al profesional clinico. Contiene nombre, apellido, especialidad, numero de licencia, telefono y email. Se relaciona con citas.

### `Cita`

Es el centro de la agenda. Contiene tipo, paciente, profesional, fecha/hora programada, estado, asistencia, motivo de cancelacion, notas y fechas de creacion/actualizacion. Mantiene una coleccion de `BitacoraCita`.

### `BitacoraCita`

Registra notas asociadas a una cita. Tiene referencia a la cita, nota y fechas de auditoria.

### `Examen`

Representa un examen solicitado. Contiene nombre, descripcion, cita asociada, fechas y documentos adjuntos.

### `DocumentoExamen`

Almacena los metadatos y el contenido binario de un documento: nombre, tipo MIME, tamaño, fecha de carga y bytes. La API devuelve metadatos en DTO y reserva los bytes para descarga.

### `OrdenMedica`

Representa una orden emitida para un paciente por un profesional, opcionalmente vinculada a una cita. Contiene tipo, descripcion y fechas.

### `Deuda`

Representa una obligacion financiera del paciente. Contiene monto total, balance, estado, paciente y fechas.

### `Pago`

Representa un abono a una deuda. Contiene monto, fecha, metodo de pago y referencia a la deuda.

### `Usuario` y `Rol`

`Usuario` contiene username, password cifrada, email y rol. `Rol` contiene nombre, activo y fechas. La password no forma parte de `UsuarioResponse`.

### Enumeraciones

- `TipoCita`: clasifica la cita, por ejemplo `CONSULTA` e `INTERCONSULTA`.
- `EstadoCita`: `PROGRAMADA`, `COMPLETADA`, `CANCELADA`, `NO_ASISTIO` y `REPROGRAMADA`.
- `TipoOrdenMedica`: clasifica la orden medica.

## 3. DTOs

Cada recurso usa pares `Request`/`Response`, salvo documentos que solo exponen `DocumentoResponse` y autenticacion que usa `LoginRequest`/`LoginResponse`.

- Pacientes: `PacienteRequest`, `PacienteResponse`.
- Profesionales: `ProfesionalRequest`, `ProfesionalResponse`.
- Citas y notas: `CitaRequest`, `CitaResponse`, `BitacoraRequest`, `BitacoraResponse`.
- Examenes y ordenes: `ExamenRequest`, `ExamenResponse`, `DocumentoResponse`, `OrdenMedicaRequest`, `OrdenMedicaResponse`.
- Finanzas: `DeudaRequest`, `DeudaResponse`, `PagoRequest`, `PagoResponse`.
- Seguridad: `UsuarioRequest`, `UsuarioResponse`, `RolRequest`, `RolResponse`, `LoginRequest`, `LoginResponse`.

`DtoMapper` centraliza la conversion de entidad a respuesta. Las relaciones se representan mediante IDs para evitar ciclos de serializacion y acoplamiento con JPA.

## 4. Controladores

| Controlador | Base | Objetivo |
|---|---|---|
| `AuthController` | `/api/v1/auth` | Login y emision de JWT. |
| `PacienteController` | `/api/v1/pacientes` | Consulta y alta de pacientes. |
| `ProfesionalController` | `/api/v1/profesionales` | Consulta y alta de profesionales. |
| `CitaController` | `/api/v1/citas` | Agenda, cancelacion, asistencia y bitacora. |
| `ExamenController` | `/api/v1/examenes` | Examenes y documentos. |
| `OrdenMedicaController` | `/api/v1/ordenes-medicas` | Ordenes medicas. |
| `DeudaController` | `/api/v1/deudas` | Consulta y alta de deudas. |
| `PagoController` | `/api/v1/pagos` | Consulta y alta de pagos. |
| `UsuarioController` | `/api/v1/usuarios` | Consulta y alta de usuarios. |
| `RolController` | `/api/v1/roles` | Consulta y alta de roles. |

## 5. Repositorios

Todos extienden `JpaRepository<Entity, Long>`. Ademas de CRUD, existen consultas para paciente por documento, citas por paciente/profesional, deudas por paciente, pagos por deuda, ordenes por paciente, documentos por examen y usuarios por username.

## 6. Seguridad y configuracion

- `JwtService`: firma, genera y valida tokens.
- `JwtAuthenticationFilter`: procesa `Authorization: Bearer` por solicitud.
- `SecurityConfig`: BCrypt, `UserDetailsService`, autenticacion stateless y rutas publicas.
- `OpenApiConfig`: informacion de Swagger y esquema `bearerAuth`.
- `DataInitializer`: datos demo para roles, usuarios y entidades principales.

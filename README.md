# Clinik

Microservicio de gestión clínica construido con Spring Boot 3.5.6, Java 25 LTS, PostgreSQL, JPA y Swagger.

## Características

- API REST bajo `/api/v1/`
- Persistencia con PostgreSQL
- Carga de archivos de exámenes en base de datos
- Gestión de usuarios, pacientes, profesionales, citas, órdenes médicas, exámenes, bitácoras, deudas y pagos
- Documentación Swagger: `/swagger-ui.html`
- Contenedores compatibles con Podman / Docker

## Endpoints principales

- `GET /api/v1/pacientes`
- `POST /api/v1/pacientes`
- `GET /api/v1/profesionales`
- `POST /api/v1/profesionales`
- `GET /api/v1/citas`
- `POST /api/v1/citas`
- `POST /api/v1/citas/{id}/asistencia`
- `POST /api/v1/citas/{id}/bitacoras`
- `GET /api/v1/examenes/{id}/documentos`
- `POST /api/v1/examenes/{id}/documentos`
- `GET /api/v1/deudas`
- `POST /api/v1/deudas`
- `GET /api/v1/pagos`
- `POST /api/v1/pagos`

## Clases principales

- `PacienteController`, `ProfesionalController`, `CitaController`
- `ExamenController`, `DeudaController`, `PagoController`
- `DocumentoExamen`, `OrdenMedica`, `Rol`, `Usuario`

## Swagger

Arranca la aplicación y visita:

- `http://localhost:8080/swagger-ui.html`

## Estructura

- `src/main/java/com/timmynet/clinik/domain` — entidades JPA
- `src/main/java/com/timmynet/clinik/repository` — repositorios Spring Data
- `src/main/java/com/timmynet/clinik/controller` — controladores REST
- `src/main/java/com/timmynet/clinik/config` — configuración y datos iniciales

## Documentación adicional

- `INSTALLATION.md` — pasos de instalación y despliegue
- `TEST_DATA.md` — datos de prueba y ejemplos con `curl`
- [`DOCUMENTACION.md`](DOCUMENTACION.md) — índice general y diagramas iniciales
- [`docs/README.md`](docs/README.md) — documentación técnica actualizada

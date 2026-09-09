# Arquitectura

El estado actual es un monolito modular Spring Boot 3.5 con Java 25 LTS. Los limites funcionales se organizan por `controller`, `service` (siguiente etapa), `repository`, `domain`, `dto`, `config` y `security`.

La separacion futura contempla Gateway, auth-service, patient-service, appointment-service y document-service. No se agregan servicios remotos hasta que existan contratos y necesidades operativas reales.

## Tecnologias

- Spring Web MVC, Data JPA, Validation y Security 6.
- PostgreSQL 16+ en produccion; H2 para pruebas.
- OpenAPI 3 en `/swagger-ui.html`.
- Podman/Podman Compose y Nginx como infraestructura objetivo.
- Thymeleaf, Bootstrap 5, ES6 y FullCalendar quedan previstos para la capa web.

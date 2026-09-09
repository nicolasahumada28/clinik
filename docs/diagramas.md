# Diagramas tecnicos

## Arquitectura actual

```mermaid
flowchart TB
    Client[Cliente REST / Swagger]
    Security[SecurityConfig + JWT Filter]
    Controllers[REST Controllers]
    DTO[Request / Response DTOs]
    Mapper[DtoMapper]
    Repositories[Spring Data Repositories]
    Domain[Entidades JPA]
    DB[(PostgreSQL / H2)]

    Client --> Security
    Security --> Controllers
    Controllers --> DTO
    Controllers --> Mapper
    Controllers --> Repositories
    Repositories --> Domain
    Domain --> DB
```

## Modulos del dominio

```mermaid
classDiagram
    Paciente "1" --> "0..*" Cita
    Profesional "1" --> "0..*" Cita
    Cita "1" --> "0..*" BitacoraCita
    Cita "1" --> "0..*" Examen
    Examen "1" --> "0..*" DocumentoExamen
    Paciente "1" --> "0..*" Deuda
    Deuda "1" --> "0..*" Pago
    Paciente "1" --> "0..*" OrdenMedica
    Profesional "1" --> "0..*" OrdenMedica
    Rol "1" --> "0..*" Usuario

    class Paciente
    class Profesional
    class Cita
    class BitacoraCita
    class Examen
    class DocumentoExamen
    class OrdenMedica
    class Deuda
    class Pago
    class Rol
    class Usuario
```

## Autenticacion

```mermaid
sequenceDiagram
    actor Usuario
    participant Auth as AuthController
    participant Manager as AuthenticationManager
    participant Repo as UsuarioRepository
    participant JWT as JwtService
    participant API as Recurso protegido

    Usuario->>Auth: POST /api/v1/auth/login
    Auth->>Manager: username + password
    Manager->>Repo: buscar username
    Repo-->>Manager: Usuario + rol
    Manager-->>Auth: autenticacion valida
    Auth->>JWT: generar token
    JWT-->>Auth: accessToken
    Auth-->>Usuario: 200 LoginResponse
    Usuario->>API: Authorization: Bearer token
    API->>JWT: validar token
    JWT-->>API: principal autenticado
```

## Programacion y ciclo de cita

```mermaid
stateDiagram-v2
    [*] --> PROGRAMADA
    PROGRAMADA --> COMPLETADA: asistencia=true
    PROGRAMADA --> NO_ASISTIO: asistencia=false
    PROGRAMADA --> CANCELADA: cancelar
    PROGRAMADA --> REPROGRAMADA: reprogramar futuro
    REPROGRAMADA --> PROGRAMADA: nueva fecha
    COMPLETADA --> [*]
    NO_ASISTIO --> [*]
    CANCELADA --> [*]
```

## Creacion de cita

```mermaid
flowchart LR
    A[Request DTO] --> B{pacienteId y profesionalId}
    B -- faltan --> E[400 Bad Request]
    B -- presentes --> C[Buscar referencias]
    C -- no existe --> F[404 Not Found]
    C -- existen --> D[Construir Cita]
    D --> G[Guardar Repository]
    G --> H[Mapear Response DTO]
    H --> I[201 Created]
```

## Alcance futuro

```mermaid
flowchart LR
    Gateway[API Gateway]
    Auth[auth-service]
    Patient[patient-service]
    Appointment[appointment-service]
    Document[document-service]
    DB[(PostgreSQL)]
    MinIO[(MinIO)]

    Gateway --> Auth
    Gateway --> Patient
    Gateway --> Appointment
    Gateway --> Document
    Auth --> DB
    Patient --> DB
    Appointment --> DB
    Document --> DB
    Document --> MinIO
```

El ultimo diagrama representa la arquitectura objetivo del roadmap, no servicios actualmente desplegados en este repositorio.

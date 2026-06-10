# Documentación del proyecto Clinik

## 1. Visión general

Clinik es un microservicio REST de gestión clínica con nombres y rutas en español.
La aplicación incluye los siguientes dominios principales:

- Pacientes (`Paciente`, `PacienteController`)
- Profesionales (`Profesional`, `ProfesionalController`)
- Citas (`Cita`, `CitaController`)
- Exámenes (`Examen`, `ExamenController`)
- Documentos de examen (`DocumentoExamen`)
- Deudas (`Deuda`, `DeudaController`)
- Pagos (`Pago`, `PagoController`)
- Roles y usuarios (`Rol`, `Usuario`)

## 2. Diagrama de clases

```mermaid
classDiagram
    class Paciente {
        Long id
        String numeroDocumento
        String nombre
        String apellido
        LocalDate fechaNacimiento
        String telefono
        String email
        String direccion
    }

    class Profesional {
        Long id
        String nombre
        String apellido
        String especialidad
        String numeroLicencia
        String telefono
        String email
    }

    class Cita {
        Long id
        TipoCita tipoCita
        EstadoCita estadoCita
        Paciente paciente
        Profesional profesional
        LocalDateTime scheduledAt
        boolean asistio
        String razonCancelacion
        String notas
    }

    class Examen {
        Long id
        String nombre
        String descripcion
        Cita cita
        LocalDateTime createdAt
    }

    class DocumentoExamen {
        Long id
        String filename
        String contentType
        Long fileSize
        LocalDateTime uploadedAt
        byte[] data
        Examen examen
    }

    class Deuda {
        Long id
        Paciente paciente
        BigDecimal montoTotal
        BigDecimal balance
        String estado
        LocalDateTime createdAt
    }

    class Pago {
        Long id
        Deuda deuda
        BigDecimal monto
        LocalDateTime fechaPago
        String metodoPago
    }

    Paciente --> Cita
    Profesional --> Cita
    Cita --> Examen
    Examen --> DocumentoExamen
    Paciente --> Deuda
    Deuda --> Pago
```

## 3. Diagrama de componentes

```mermaid
flowchart TB
    subgraph APIREST
        PC[PacienteController]
        PR[ProfesionalController]
        CC[CitaController]
        EC[ExamenController]
        DC[DeudaController]
        PC2[PagoController]
        UC[UsuarioController]
        RC[RolController]
    end

    subgraph Dominio
        Pac[PacienteRepository]
        Prof[ProfesionalRepository]
        Cit[CitaRepository]
        Exa[ExamenRepository]
        Doc[DocumentoExamenRepository]
        Deu[DeudaRepository]
        Pag[PagoRepository]
        Usu[UsuarioRepository]
        Rol[RolRepository]
    end

  
    PC --> Pac
    PR --> Prof
    CC --> Cit
    EC --> Exa
    EC --> Doc
    DC --> Deu
    PC2 --> Pag
    UC --> Usu
    RC --> Rol
```

## 4. Diagrama de casos de uso

```mermaid
flowchart LR
    actorPaciente[Paciente / Recepcionista]
    actorAdmin[Administrador]
    actorContabilidad[Contabilidad]

    actorPaciente -->|Registrar paciente| usecase1[Crear paciente]
    actorPaciente -->|Solicitar cita| usecase2[Programar cita]
    actorPaciente -->|Enviar asistencia| usecase3[Registrar asistencia]
    actorPaciente -->|Subir examen| usecase4[Subir documento de examen]
    actorContabilidad -->|Emitir deuda| usecase5[Crear deuda]
    actorContabilidad -->|Registrar pago| usecase6[Registrar pago]
    actorAdmin -->|Administrar roles| usecase7[Gestionar roles]
```

## 5. Diagrama de actividad: flujo de creación de cita

```mermaid
flowchart TD
    A[Inicio]
    B[Ingresar datos de paciente]
    C[Ingresar datos de profesional]
    D[Enviar solicitud POST /api/v1/citas]
    E[Crear objeto Cita]
    F[Guardar Cita en base de datos]
    G[Respuesta 201 con Cita creada]
    H[Fin]

    A --> B --> C --> D --> E --> F --> G --> H
```

## 6. Diagrama de actividad: pago de deuda

```mermaid
flowchart TD
    A[Inicio]
    B[Obtener deuda existente]
    C[Enviar solicitud POST /api/v1/pagos]
    D[Crear Pago]
    E[Actualizar balance de Deuda]
    F[Marcar Deuda como PAGADA si balance = 0]
    G[Guardar Pago]
    H[Respuesta 201 con Pago creado]
    I[Fin]

    A --> B --> C --> D --> E --> F --> G --> H --> I
```

## 7. Diagrama de secuencia: programación de cita

```mermaid
sequenceDiagram
    actor Paciente
    participant API as CitaController
    participant Repo as CitaRepository
    participant DB as BD (PostgreSQL)

    Paciente->>API: POST /api/v1/citas
    API->>Repo: save(cita)
    Repo->>DB: insert cita
    DB-->>Repo: confirmación
    Repo-->>API: cita guardada
    API-->>Paciente: 201 Created
```

## 8. Diagrama BPMN: flujo de validación y pago

```mermaid
flowchart LR
    Start((Inicio))
    A[Validar entrada de pago]
    B{¿Deuda existe?}
    C[Crear registro de pago]
    D[Actualizar deuda]
    E{¿Balance = 0?}
    F[Estado = PAGADA]
    G[Guardar transacción]
    End((Fin))

    Start --> A --> B
    B -- Sí --> C --> D --> E
    E -- Sí --> F --> G --> End
    E -- No --> G --> End
    B -- No --> End
```

## 9. Notas de documentación

- Las rutas REST se exponen en español bajo `/api/v1/`.
- Las clases y entidades principales están localizadas en español para reflejar el dominio clínico.
- El esquema de datos usa `numeroDocumento`, `fechaNacimiento`, `montoTotal`, `metodoPago`, `documentos`, `ordenes-medicas` y `bitacoras`.

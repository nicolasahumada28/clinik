# Instalación y despliegue

## Requisitos

- Java 25 (LTS)
- Maven
- PostgreSQL
- Podman o Docker

Verifica la versión instalada antes de ejecutar el proyecto:

```bash
java -version
```

La salida debe indicar Java 25. El proyecto configura este requisito en `pom.xml` mediante `java.version` y `maven.compiler.release`.

## Base de datos PostgreSQL

1. Crea la base de datos:

```bash
createdb -h localhost -p 5432 -U postgres clinikdb
```

2. Usa las credenciales por defecto en `src/main/resources/application.properties`:

- Usuario: `postgres`
- Contraseña: `postgres`

## Ejecutar localmente

```bash
./mvnw spring-boot:run
```

La aplicación quedará disponible en `http://localhost:8080`.

## Ejecutar con Podman / Docker

```bash
podman compose -f podman-compose.yml up --build
```

O con Docker:

```bash
docker compose up --build
```

## Swagger UI

- `http://localhost:8080/swagger-ui.html`

## Postman / curl

Ver `TEST_DATA.md` para ejemplos de solicitudes y datos de prueba.

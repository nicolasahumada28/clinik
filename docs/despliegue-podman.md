# Despliegue Podman

El proyecto conserva `Dockerfile`, `docker-compose.yml` y `podman-compose.yml`. Los Compose usan PostgreSQL 16 y esperan a que la base de datos esté lista antes de iniciar la aplicación. PostgreSQL debe exponerse como `clinikdb` y configurarse con `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`.

Define `CLINIK_JWT_SECRET` con un secreto Base64 de al menos 256 bits antes de un despliegue no local. Los archivos Compose mantienen un valor de desarrollo únicamente como fallback.

En produccion, añadir Nginx como reverse proxy y reemplazar el secreto JWT de desarrollo. MinIO y Apache HOP quedan previstos para las siguientes fases.

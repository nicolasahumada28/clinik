# ===== FASE DE BUILD =====
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /workspace

# Copiar archivos base de Maven
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn

# Dar permisos al wrapper
RUN chmod +x mvnw

# Limpiar y descargar dependencias (cache eficiente)
RUN ./mvnw clean dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar el proyecto (empaquetar)
RUN ./mvnw clean package -DskipTests -B

# ===== FASE DE RUNTIME =====
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copiar el JAR generado
COPY --from=build /workspace/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables útiles (opcional)
ENV JAVA_OPTS=""

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
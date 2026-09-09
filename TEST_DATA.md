# Datos de prueba y ejemplos de endpoints

## Datos iniciales

Al iniciar la aplicación, se crean automáticamente los siguientes datos de prueba:

- Usuario `admin` / `admin123`
- Usuario `reception` / `recep123`
- Usuario `billing` / `billing123`
- Paciente `Juan Perez`
- Profesional `María González`
- Cita médica programada
- Orden médica de examen
- Factura abierta

## Ejemplos `curl`

### Crear paciente

```bash
curl -X POST http://localhost:8080/api/v1/pacientes \
  -H "Content-Type: application/json" \
  -d '{"numeroDocumento":"987654321","nombre":"Ana","apellido":"Lopez","fechaNacimiento":"1990-06-04","telefono":"+56911223344","email":"ana.lopez@example.com","direccion":"Calle 123"}'
```

### Crear profesional

```bash
curl -X POST http://localhost:8080/api/v1/profesionales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Carlos","apellido":"Ramirez","especialidad":"Cardiología","numeroLicencia":"MED-002","telefono":"+56966778899","email":"carlos.ramirez@clinik.local"}'
```

### Agendar cita

```bash
curl -X POST http://localhost:8080/api/v1/citas \
  -H "Content-Type: application/json" \
  -d '{"tipoCita":"CONSULTA","paciente":{"id":1},"profesional":{"id":1},"scheduledAt":"2026-06-10T10:30:00","notas":"Consulta inicial"}'
```

### Registrar asistencia

```bash
curl -X POST "http://localhost:8080/api/v1/citas/1/asistencia?attended=true"
```

### Cancelar cita

```bash
curl -X PUT "http://localhost:8080/api/v1/citas/1/cancelar?reason=Paciente%20solicito%20cambio"
```

### Agregar bitácora a cita

```bash
curl -X POST http://localhost:8080/api/v1/citas/1/bitacoras \
  -H "Content-Type: application/json" \
  -d '{"nota":"Paciente con síntomas leves. Se prescribe seguimiento."}'
```

### Crear orden médica

```bash
curl -X POST http://localhost:8080/api/v1/ordenes-medicas \
  -H "Content-Type: application/json" \
  -d '{"tipoOrdenMedica":"EXAMEN","paciente":{"id":1},"profesional":{"id":1},"cita":{"id":1},"descripcion":"Orden de laboratorio completo"}'
```

### Crear deuda

```bash
curl -X POST http://localhost:8080/api/v1/deudas \
  -H "Content-Type: application/json" \
  -d '{"paciente":{"id":1},"montoTotal":150000}'
```

### Registrar pago

```bash
curl -X POST http://localhost:8080/api/v1/pagos \
  -H "Content-Type: application/json" \
  -d '{"deuda":{"id":1},"monto":50000,"metodoPago":"TRANSFERENCIA"}'
```

### Subir documento de examen

```bash
curl -X POST http://localhost:8080/api/v1/examenes/1/documentos \
  -F file=@./reporte.pdf
```

### Descargar documento de examen

```bash
curl -L http://localhost:8080/api/v1/examenes/1/documentos/1 --output reporte-descargado.pdf
```

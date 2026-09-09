# ADR-001: Monolito modular inicial

## Estado

Aceptada.

## Decision

Construir el MVP como monolito modular con contratos DTO y limites de dominio claros. Gateway y microservicios se incorporaran cuando exista una necesidad de escalamiento o despliegue independiente.

## Consecuencias

El desarrollo inicial es mas simple y transaccional. Los DTOs y los modulos reducen el acoplamiento necesario para extraer servicios posteriormente.

# saas-booking-engine-multitenant-appointment-core.
Production-ready Multi-Tenant Booking &amp; Service Management Platform. Implements DDD/Clean Architecture, JWT multi-role security, JSONB metadata flexibility, and out-of-the-box automation webhooks.

## Desarrollo local

Levanta las dependencias con `docker compose up -d` y ejecuta el backend desde `booking-engine` con `./mvnw spring-boot:run`.

El registro público solo permite usuarios `CUSTOMER`. Para crear el primer `SUPER_ADMIN`, inicia temporalmente el backend con estas variables de entorno:

```text
BOOTSTRAP_SUPER_ADMIN_ENABLED=true
BOOTSTRAP_SUPER_ADMIN_EMAIL=admin@example.com
BOOTSTRAP_SUPER_ADMIN_PASSWORD=una-clave-segura
```

El bootstrap es idempotente: no crea otro usuario si ya existe un `SUPER_ADMIN` con ese email. Después del primer arranque se recomienda desactivar `BOOTSTRAP_SUPER_ADMIN_ENABLED`.

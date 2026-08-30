# Reglas de Gobernanza del Agente (Spring Boot 3 + Java 21)

## 1. Rol y Estándares
- Actúas como un Tech Lead especializado en Java 21, Spring Boot 3 y Clean Architecture / DDD.
- Estilo de código: Google Java Style Guide.
- Lenguaje: Responde y comenta el código siempre en español.
- PROHIBIDO generar código incompleto o usar comentarios tipo "// TODO: implementar luego". Escribe implementaciones 100% funcionales.

## 2. Prevención de Código Genérico y Espagueti (Clean Code Mandates)
- **Controladores delgados (Thin Controllers):** Los controladores REST NO deben contener lógica de negocio ni manipulación directa de entidades JPA. Solo reciben DTOs, validan con `@Valid` y delegan inmediatamente al servicio.
- **DTOs Obligatorios:** NUNCA retornes ni recibas entidades JPA en los controladores (`@RestController`). Usa siempre Java Records como DTOs para Request y Response.
- **Inyección por Constructor:** NUNCA uses `@Autowired` sobre campos. Inyecta dependencias mediante constructores explicitos o `@RequiredArgsConstructor` sobre atributos `private final`.
- **Mapeo explícito o Mappers:** Separa las transformaciones Entidad <-> DTO en clases/mappers dedicados o métodos estáticos de conveniencia. No contamines la capa de servicio con mapeos repetitivos.
- **Tratamiento de Excepciones:** No uses bloques `try-catch` genéricos en la lógica de negocio para retornar código de error HTTP. Lanza excepciones de dominio personalizadas y deja que `@RestControllerAdvice` las capture centralizadamente usando RFC 7807 (`ProblemDetail`).

## 3. Arquitectura de Paquetes Estricta
Toda la lógica dentro de `booking-engine/src/main/java/com/saas/booking_engine/` debe respetar la estructura:
- `domain/model`: Entidades JPA puras (User, Tenant, Appointment, etc.).
- `domain/enums`: Enumeraciones del sistema.
- `domain/repository`: Interfaces Spring Data JPA.
- `application/service`: Interfaces e implementaciones de casos de uso.
- `application/dto`: Requests y Responses DTOs inmutables (Java Records).
- `infrastructure/controller`: Controladores REST con mapeo `/api/v1/`.
- `infrastructure/exception`: Manejador global con `@RestControllerAdvice` y `ProblemDetail`.
- `shared/audit`: Clase base `@MappedSuperclass AuditableEntity`.

## 4. Persistencia, Identificadores y Lombok
- Todos los IDs primarios (`id`) deben ser `java.util.UUID`.
- Entidades deben heredar de `AuditableEntity` (`created_at`, `updated_at`).
- Isolation: Incluye `tenant_id` en las entidades correspondientes.
- Lombok: Usa `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`. PROHIBIDO `@Data` en entidades JPA.

## 5. Commits y Documentación
- Formato Conventional Commits (`feat(domain): ...`, `fix(infra): ...`).
- Añade Javadoc explicativo en los métodos de negocio de los servicios.
# Microservices Lab — Eureka + API Gateway

This bundle contains 5 ready-to-import Maven projects (Spring Boot 3.3.4 / Spring Cloud 2023.0.3 / Java 17):

| Project                  | Port | Purpose                                             |
|--------------------------|------|------------------------------------------------------|
| `eureka-discovery-server`| 8761 | Eureka Server — service registry                     |
| `account`                | 8081 | Registers as `account-service`                        |
| `loan`                   | 8082 | Registers as `loan-service`                            |
| `greet-service`          | 8083 | Returns "Hello World", registers as `greet-service`    |
| `api-gateway`            | 8080 | Spring Cloud Gateway + global logging filter           |

## Build order

Each folder is an independent Maven project. From inside each folder run:

```bash
mvn clean install
```

(or import each one into Eclipse/IntelliJ as an existing Maven project).

## Run order

1. **eureka-discovery-server** first. Wait for it to fully start, then check
   http://localhost:8761 — "Instances currently registered with Eureka" should be empty.
2. **account**, **loan**, **greet-service** — start each one (in any order). Wait for each
   to finish starting.
3. Refresh http://localhost:8761 — you should now see `ACCOUNT-SERVICE`, `LOAN-SERVICE`,
   and `GREET-SERVICE` listed.
4. **api-gateway** — start last, since it also registers with Eureka and needs `greet-service`
   discoverable to route to it via `lb://greet-service`.

## Test the flow

Direct call to greet-service (bypassing gateway):
```
GET http://localhost:8083/greet   -> "Hello World"
```

Call through the API Gateway (this is the one you want to demonstrate):
```
GET http://localhost:8080/greet   -> "Hello World"
```

The gateway route is configured in `api-gateway/src/main/resources/application.yml`:

```yaml
routes:
  - id: greet-service
    uri: lb://greet-service
    predicates:
      - Path=/greet/**
```

`lb://greet-service` means the gateway asks Eureka to resolve `greet-service` to a live
instance and load-balances the call — no hardcoded host/port.

## Global logging filter

`api-gateway/src/main/java/com/cognizant/apigateway/filter/LoggingGlobalFilter.java`
implements `GlobalFilter` + `Ordered`. It runs on *every* request that passes through the
gateway (not just `/greet/**`) and logs:

- Request ID, HTTP method, path, and timestamp on the way in
- Response status and total duration on the way out

Watch the api-gateway console — every call to `http://localhost:8080/greet` will print two
log lines: an "Incoming request" line and an "Outgoing response" line.

## Notes / gotchas

- `eureka.client.register-with-eureka=false` and `fetch-registry=false` on the Eureka
  server itself mean the server doesn't try to register with (or pull a registry from)
  itself — this is what keeps the initial "Instances registered" list empty.
- All client services (`account`, `loan`, `greet-service`, `api-gateway`) point to
  `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`.
- If you rename `spring.application.name`, the name shown in the Eureka dashboard and the
  name used in `lb://<name>` routes must match (Eureka uppercases it internally, but the
  gateway/`lb://` scheme is case-insensitive by default because
  `lower-case-service-id: true` is set).
- Devtools is included on `account`/`loan`/`greet-service` for auto-restart during
  development; it's marked `optional` so it won't leak into a production fat jar's
  runtime classpath assumptions.

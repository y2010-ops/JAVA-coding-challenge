# Customer Management API

A Spring Boot RESTful API to manage customers with on-the-fly membership **tier** calculation.

## Tech
- Spring Boot 3, Java 17
- H2 in-memory DB
- OpenAPI via springdoc (UI at `/swagger-ui.html`)
- Maven

## Run (Cursor IDE)
1. Open this folder in Cursor.
2. Open terminal and run:
   ```bash
   mvn spring-boot:run
   ```
3. Explore:
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, pass: blank)

## Endpoints (samples)
```bash
# Create
curl -X POST http://localhost:8080/customers -H "Content-Type: application/json" -d '{
  "name": "Alice",
  "email": "alice@example.com",
  "annualSpend": 1200,
  "lastPurchaseDate": "2025-05-01T10:00:00Z"
}'

# Get by id
curl http://localhost:8080/customers/{id}

# Get by name
curl "http://localhost:8080/customers?name=Alice"

# Get by email
curl "http://localhost:8080/customers?email=alice@example.com"

# Update
curl -X PUT http://localhost:8080/customers/{id} -H "Content-Type: application/json" -d '{
  "name": "Alice Smith",
  "email": "alice@example.com",
  "annualSpend": 10500,
  "lastPurchaseDate": "2025-07-10T00:00:00Z"
}'

# Delete
curl -X DELETE http://localhost:8080/customers/{id} -i
```

## Tier Logic
- **Silver**: annualSpend < 1000 (or missing / too old lastPurchaseDate)
- **Gold**: 1000 ≤ annualSpend < 10000 **and** lastPurchaseDate within last 12 months
- **Platinum**: annualSpend ≥ 10000 **and** lastPurchaseDate within last 6 months

> Tier is calculated at read time and not stored in DB.

## Assumptions
- Email is unique.
- `POST /customers` does not allow `id` in payload (request DTO has no `id` field).
- `lastPurchaseDate` is ISO-8601 (`OffsetDateTime`).

## Tests
Run tests:
```bash
mvn test
```

Coverage:
- CRUD via MockMvc
- Tier calculation
- Email validation
```

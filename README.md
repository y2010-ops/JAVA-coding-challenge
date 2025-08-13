# Customer Management API

A Spring Boot RESTful API to manage customers with on-the-fly membership **tier** calculation.

## Tech
- Spring Boot 3, Java 17
- H2 in-memory DB
- OpenAPI via springdoc (UI at `/swagger-ui.html`)
- Maven

## Prerequisites
- Java 17 or higher
- Maven 3.6+ (or use the Maven wrapper included in the project)
- Any IDE (IntelliJ IDEA, Eclipse, VS Code, Cursor, etc.)

## Run Locally

### Option 1: Command Line (Any IDE/Terminal)
1. Clone or download the project
2. Navigate to the project directory:
   ```bash
   cd JAVA-coding-challenge
   ```
3. Run using Maven:
   ```bash
   mvn spring-boot:run
   ```
   Or using the Maven wrapper (if available):
   ```bash
   ./mvnw spring-boot:run
   ```

### Option 2: IDE Specific Instructions

#### IntelliJ IDEA
1. Open IntelliJ IDEA
2. File → Open → Select the project folder
3. Wait for Maven dependencies to download
4. Run the main class: `CustomerManagementApiApplication.java`
   - Right-click on the class → Run 'CustomerManagementApiApplication'
   - Or use the green play button in the gutter

#### Eclipse
1. Open Eclipse
2. File → Import → Existing Maven Projects
3. Browse and select the project folder
4. Wait for Maven dependencies to download
5. Right-click on `CustomerManagementApiApplication.java` → Run As → Java Application

#### VS Code
1. Install the "Extension Pack for Java" extension
2. Open the project folder in VS Code
3. Open `CustomerManagementApiApplication.java`
4. Click "Run" above the main method or press F5

#### Cursor IDE
1. Open this folder in Cursor
2. Open terminal and run:
   ```bash
   mvn spring-boot:run
   ```

### Option 3: Build and Run JAR
```bash
# Build the project
mvn clean package

# Run the generated JAR
java -jar target/customer-management-api-0.0.1-SNAPSHOT.jar
```

## Access the Application
Once running, explore:
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console 
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (leave blank)

## API Endpoints (Examples)
```bash
# Create a customer
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "email": "alice@example.com",
    "annualSpend": 1200,
    "lastPurchaseDate": "2025-05-01T10:00:00Z"
  }'

# Get customer by ID
curl http://localhost:8080/customers/{id}

# Get customer by name
curl "http://localhost:8080/customers?name=Alice"

# Get customer by email
curl "http://localhost:8080/customers?email=alice@example.com"

# Update a customer
curl -X PUT http://localhost:8080/customers/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Smith",
    "email": "alice@example.com",
    "annualSpend": 10500,
    "lastPurchaseDate": "2025-07-10T00:00:00Z"
  }'

# Delete a customer
curl -X DELETE http://localhost:8080/customers/{id} -i
```

## Tier Logic
- **Silver**: annualSpend < 1000 (or missing/too old lastPurchaseDate)
- **Gold**: 1000 ≤ annualSpend < 10000 **and** lastPurchaseDate within last 12 months
- **Platinum**: annualSpend ≥ 10000 **and** lastPurchaseDate within last 6 months

> Tier is calculated at read time and not stored in the database.

## Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run tests with verbose output
mvn test -Dtest=CustomerApiTests

# Run a specific test method
mvn test -Dtest=CustomerApiTests#create_and_get_by_id
```

### Test Coverage
The test suite includes:
- CRUD operations via MockMvc
- Tier calculation boundary testing
- Email validation
- Error handling scenarios

## Project Structure
```
src/
├── main/java/com/example/customers/
│   ├── CustomerManagementApiApplication.java  # Main application class
│   ├── config/                                # Configuration classes
│   ├── controller/                            # REST controllers
│   ├── dto/                                   # Data Transfer Objects
│   ├── model/                                 # JPA entities
│   ├── repository/                            # Data repositories
│   └── service/                               # Business logic
├── main/resources/
│   ├── application.properties                 # App configuration
│   └── openapi.yaml                          # API specification
└── test/java/                                # Test classes
```

## Configuration Notes
- **Database**: H2 in-memory database (data is lost on restart)
- **Port**: Application runs on port 8080 by default
- **Profiles**: Uses default profile (no additional profiles configured)

## Troubleshooting

### Common Issues
1. **Port 8080 already in use**: 
   - Kill the process using the port or change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

2. **Java version issues**:
   - Ensure Java 17+ is installed: `java -version`
   - Set JAVA_HOME environment variable if needed

3. **Maven issues**:
   - Verify Maven installation: `mvn -version`
   - Clean and rebuild: `mvn clean compile`

4. **IDE not recognizing project**:
   - Reimport the Maven project
   - Refresh dependencies
   - Check that the IDE supports Java 17

## API Design Assumptions
- Email addresses are unique across all customers
- `POST /customers` endpoint does not accept `id` in the request payload
- `lastPurchaseDate` follows ISO-8601 format (`OffsetDateTime`)
- Tier calculation is performed dynamically on every read operation

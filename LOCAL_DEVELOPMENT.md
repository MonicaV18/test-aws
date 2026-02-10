# Spring Cloud Function with Podman - Local Development Guide

This guide covers running the Spring Cloud Function application locally using Podman with MySQL and Elasticsearch.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Podman and podman-compose installed
- (Optional) Docker Compose (podman-compose is recommended)

## Installing Podman

### On Linux
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install -y podman podman-compose

# Fedora/RHEL
sudo dnf install -y podman podman-compose
```

### On macOS
```bash
brew install podman podman-compose

# Initialize Podman machine
podman machine init
podman machine start
```

### On Windows
Download and install Podman Desktop from: https://podman.io/getting-started/installation

## Project Structure

```
.
├── Dockerfile                          # Container image for the app
├── podman-compose.yml                  # Podman compose configuration
├── pom.xml                            # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/example/lambda/
│   │   │   ├── LambdaApplication.java          # Main application
│   │   │   ├── model/
│   │   │   │   ├── RequestEntity.java          # MySQL entity
│   │   │   │   └── RequestDocument.java        # Elasticsearch document
│   │   │   ├── repository/
│   │   │   │   ├── RequestRepository.java      # JPA repository
│   │   │   │   └── RequestDocumentRepository.java  # ES repository
│   │   │   └── service/
│   │   │       └── RequestProcessingService.java   # Business logic
│   │   └── resources/
│   │       ├── application.properties          # Default config
│   │       └── application-local.properties    # Local dev config
```

## Running Locally with Podman

### Option 1: Full Stack with Podman Compose (Recommended)

1. **Build the application**:
```bash
mvn clean package -DskipTests
```

2. **Start all services** (MySQL, Elasticsearch, and App):
```bash
podman-compose up -d
```

3. **Check service status**:
```bash
podman-compose ps
```

4. **View logs**:
```bash
# All services
podman-compose logs -f

# Specific service
podman-compose logs -f app
podman-compose logs -f mysql
podman-compose logs -f elasticsearch
```

5. **Test the application**:
```bash
curl -X POST http://localhost:8080/processRequest \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","message":"Hello from Podman"}'
```

6. **Stop all services**:
```bash
podman-compose down
```

7. **Stop and remove volumes** (clean slate):
```bash
podman-compose down -v
```

### Option 2: Run Infrastructure Only (for development)

Run just MySQL and Elasticsearch, then run the app from your IDE:

1. **Start infrastructure services**:
```bash
podman-compose up -d mysql elasticsearch
```

2. **Wait for services to be healthy**:
```bash
# Check MySQL
podman exec -it spring-cloud-mysql mysqladmin ping -h localhost -u springuser -pspringpass

# Check Elasticsearch
curl http://localhost:9200/_cluster/health
```

3. **Run the application** from your IDE or command line:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

4. **Stop infrastructure**:
```bash
podman-compose stop mysql elasticsearch
```

## Database Access

### MySQL Connection Details
- **Host**: localhost
- **Port**: 3306
- **Database**: springcloud
- **Username**: springuser
- **Password**: springpass

### Connect to MySQL CLI
```bash
podman exec -it spring-cloud-mysql mysql -u springuser -pspringpass springcloud
```

### Useful MySQL Commands
```sql
-- List all tables
SHOW TABLES;

-- View requests table
SELECT * FROM requests;

-- Count records
SELECT COUNT(*) FROM requests;

-- Exit
EXIT;
```

## Elasticsearch Access

### Elasticsearch Connection Details
- **URL**: http://localhost:9200
- **No authentication** (development only)

### Elasticsearch Commands
```bash
# Check cluster health
curl http://localhost:9200/_cluster/health?pretty

# View all indices
curl http://localhost:9200/_cat/indices?v

# Search request-logs index
curl http://localhost:9200/request-logs/_search?pretty

# View specific document
curl http://localhost:9200/request-logs/_doc/{document-id}?pretty
```

## Testing the Application

### Using curl
```bash
# Basic request
curl -X POST http://localhost:8080/processRequest \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","message":"Test message"}'

# Expected response
{
  "message": "Hello Alice! Your request has been processed successfully.",
  "status": "SUCCESS",
  "timestamp": 1706024897113
}
```

### Verify Data Persistence

1. **Check MySQL**:
```bash
podman exec -it spring-cloud-mysql mysql -u springuser -pspringpass springcloud -e "SELECT * FROM requests;"
```

2. **Check Elasticsearch**:
```bash
curl http://localhost:9200/request-logs/_search?pretty
```

## Troubleshooting

### Podman Machine Not Running (macOS/Windows)
```bash
podman machine start
```

### Port Already in Use
```bash
# Find process using port 3306 or 9200
lsof -i :3306
lsof -i :9200

# Or modify podman-compose.yml to use different ports
```

### Database Connection Issues
```bash
# Check if MySQL is healthy
podman-compose ps
podman logs spring-cloud-mysql

# Restart MySQL
podman-compose restart mysql
```

### Elasticsearch Not Ready
```bash
# Check Elasticsearch logs
podman logs spring-cloud-elasticsearch

# Wait for Elasticsearch to be ready
curl http://localhost:9200/_cluster/health?wait_for_status=yellow&timeout=60s
```

### Application Won't Start
```bash
# Check application logs
podman logs spring-cloud-function-app

# Verify dependencies are running
podman-compose ps

# Rebuild application
mvn clean package -DskipTests
podman-compose up -d --build app
```

### Clean Start
```bash
# Remove all containers and volumes
podman-compose down -v

# Remove all images
podman image prune -a

# Start fresh
mvn clean package -DskipTests
podman-compose up -d
```

## Development Workflow

1. **Make code changes** in your IDE
2. **Rebuild**:
```bash
mvn clean package -DskipTests
```
3. **Restart app container**:
```bash
podman-compose restart app
```
4. **View logs**:
```bash
podman-compose logs -f app
```

## Switching Between Local and AWS Lambda

The application is designed to work in both environments:

### Local Development Mode
- Uses `local` profile
- Connects to PostgreSQL and Elasticsearch
- Web server enabled on port 8080
- Full logging enabled

### AWS Lambda Mode  
- Uses default profile
- DB/ES connections are optional (gracefully disabled if not available)
- No web server
- Optimized for Lambda runtime

## Next Steps

- Deploy to AWS Lambda (see main README.md)
- Add custom functions
- Implement additional business logic
- Add authentication and security
- Configure monitoring and alerts

## Additional Resources

- [Podman Documentation](https://docs.podman.io/)
- [Spring Cloud Function](https://spring.io/projects/spring-cloud-function)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Data Elasticsearch](https://spring.io/projects/spring-data-elasticsearch)

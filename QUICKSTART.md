# Quick Start Guide

Get up and running with Spring Cloud Function + Podman in 5 minutes!

## Prerequisites

- Java 17+
- Maven 3.6+
- Podman (or Docker)

## Installation

### Install Podman

**macOS:**
```bash
brew install podman podman-compose
podman machine init
podman machine start
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install -y podman podman-compose
```

**Linux (Fedora/RHEL):**
```bash
sudo dnf install -y podman podman-compose
```

**Windows:**
Download from https://podman.io/getting-started/installation

## Quick Start

### Option 1: Using Helper Script (Recommended)

```bash
# Interactive menu
./local-dev.sh

# Or direct commands
./local-dev.sh start    # Build and start everything
./local-dev.sh test     # Test the application
./local-dev.sh stop     # Stop all services
```

### Option 2: Manual Commands

```bash
# 1. Build the application
mvn clean package -DskipTests

# 2. Start all services (MySQL, Elasticsearch, App)
podman-compose up -d

# 3. Check logs
podman-compose logs -f

# 4. Test the application
curl -X POST http://localhost:8080/processRequest \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","message":"Hello World"}'

# 5. Stop services
podman-compose down
```

### Option 3: Using Docker

If you prefer Docker over Podman:

```bash
# Replace podman-compose with docker-compose
docker-compose up -d
docker-compose logs -f
docker-compose down
```

## Verify Everything Works

### 1. Check Services are Running

```bash
podman-compose ps
```

You should see:
- `spring-cloud-mysql` (healthy)
- `spring-cloud-elasticsearch` (healthy)
- `spring-cloud-function-app` (running)

### 2. Test the Application

```bash
curl -X POST http://localhost:8080/processRequest \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","message":"Testing"}'
```

Expected response:
```json
{
  "message": "Hello Alice! Your request has been processed successfully.",
  "status": "SUCCESS",
  "timestamp": 1706024897113
}
```

### 3. Verify Data in MySQL

```bash
podman exec -it spring-cloud-mysql mysql -u springuser -pspringpass springcloud

# In MySQL shell:
SELECT * FROM requests;
EXIT;
```

### 4. Verify Data in Elasticsearch

```bash
curl http://localhost:9200/request-logs/_search?pretty
```

## Common Issues

### Port Already in Use

If ports 3306, 9200, or 8080 are already in use:

1. Edit `podman-compose.yml` or `docker-compose.yml`
2. Change the port mappings:
   ```yaml
   ports:
     - "13306:3306"  # MySQL
     - "19200:9200"  # Elasticsearch
     - "18080:8080"  # Application
   ```

### Podman Machine Not Started (macOS/Windows)

```bash
podman machine start
```

### Services Not Ready

Wait a bit longer for services to be healthy:

```bash
# Check health status
podman-compose ps

# View logs for specific service
podman-compose logs mysql
podman-compose logs elasticsearch
```

### Clean Restart

```bash
# Stop and remove everything including volumes
podman-compose down -v

# Rebuild and restart
mvn clean package -DskipTests
podman-compose up -d --build
```

## Development Workflow

1. **Make code changes** in your IDE
2. **Rebuild** the application:
   ```bash
   mvn clean package -DskipTests
   ```
3. **Restart** the app container:
   ```bash
   podman-compose restart app
   ```
4. **Test** your changes:
   ```bash
   curl -X POST http://localhost:8080/processRequest \
     -H "Content-Type: application/json" \
     -d '{"name":"Test","message":"My changes"}'
   ```

## Running Without Containers

To run just the infrastructure (DB + ES) and run the app from your IDE:

```bash
# Start only infrastructure
podman-compose up -d mysql elasticsearch

# Run app from IDE or command line
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Next Steps

- 📖 Read [LOCAL_DEVELOPMENT.md](LOCAL_DEVELOPMENT.md) for detailed documentation
- 🚀 Deploy to AWS Lambda (see main [README.md](README.md))
- 🔧 Customize the application for your use case
- 📊 Add monitoring and observability

## Getting Help

- Check logs: `podman-compose logs -f`
- Verify services: `podman-compose ps`
- Restart services: `podman-compose restart [service-name]`
- Full documentation: [LOCAL_DEVELOPMENT.md](LOCAL_DEVELOPMENT.md)

## Troubleshooting

**Problem:** Application can't connect to MySQL
```bash
# Check MySQL is healthy
podman exec -it spring-cloud-mysql mysqladmin ping -h localhost -u springuser -pspringpass

# View MySQL logs
podman logs spring-cloud-mysql
```

**Problem:** Application can't connect to Elasticsearch
```bash
# Check Elasticsearch health
curl http://localhost:9200/_cluster/health

# View Elasticsearch logs
podman logs spring-cloud-elasticsearch
```

**Problem:** "Connection refused" errors
- Ensure all services are running: `podman-compose ps`
- Wait for services to be healthy (can take 30-60 seconds on first start)
- Check if ports are available: `lsof -i :3306,9200,8080`

Happy coding! 🎉

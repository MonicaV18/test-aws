# How to Run and Test This Application Locally with Podman

This guide provides step-by-step instructions for running and testing the AWS Lambda Spring Boot application locally using Podman.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17 or higher** - [Download Java](https://adoptium.net/)
- **Maven 3.6 or higher** - [Download Maven](https://maven.apache.org/download.cgi)
- **Podman** - Container management tool (Podman is a Docker alternative)
- **podman-compose** - Tool for defining multi-container applications

## Step 1: Install Podman

### On macOS
```bash
# Install Podman and podman-compose
brew install podman podman-compose

# Initialize and start Podman machine
podman machine init
podman machine start
```

### On Linux (Ubuntu/Debian)
```bash
sudo apt-get update
sudo apt-get install -y podman podman-compose
```

### On Linux (Fedora/RHEL)
```bash
sudo dnf install -y podman podman-compose
```

### On Windows
Download and install Podman Desktop from [https://podman.io/getting-started/installation](https://podman.io/getting-started/installation)

## Step 2: Verify Installation

```bash
# Check Podman version
podman --version

# Check podman-compose version
podman-compose --version

# Verify Podman is running (macOS/Windows)
podman machine list
```

## Step 3: Run the Application

### Option A: Using the Helper Script (Easiest)

The repository includes a helper script that automates the entire process:

```bash
# Make the script executable (if needed)
chmod +x local-dev.sh

# Start everything with one command
./local-dev.sh start
```

This command will:
1. Build the application using Maven
2. Start MySQL database container
3. Start Elasticsearch container
4. Start the application container
5. Wait for all services to be healthy

### Option B: Manual Steps

If you prefer to run commands manually:

```bash
# 1. Build the application
mvn clean package -DskipTests

# 2. Start all services (MySQL, Elasticsearch, and the application)
podman-compose up -d

# 3. Check that all containers are running
podman-compose ps

# 4. View logs (optional)
podman-compose logs -f
```

## Step 4: Test the Application

### Quick Test

Once the application is running, test it with a simple HTTP request:

```bash
curl -X POST http://localhost:8080/processRequest \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","message":"Testing locally with Podman"}'
```

**Expected Response:**
```json
{
  "message": "Hello John Doe! Your request has been processed successfully.",
  "status": "SUCCESS",
  "timestamp": 1706024897113
}
```

### Verify Data Storage

#### Check MySQL Database
```bash
# Connect to MySQL container
podman exec -it spring-cloud-mysql mysql -u springuser -pspringpass springcloud

# Inside MySQL shell, view stored requests
SELECT * FROM requests;

# Exit MySQL
EXIT;
```

#### Check Elasticsearch
```bash
# View all indexed documents
curl http://localhost:9200/request-logs/_search?pretty
```

### Run Automated Tests

```bash
# Run the test suite
mvn test
```

## Step 5: Stop the Application

### Using the Helper Script
```bash
./local-dev.sh stop
```

### Manual Command
```bash
podman-compose down
```

### Clean Stop (Remove Data Volumes)
```bash
# This will remove all data from MySQL and Elasticsearch
podman-compose down -v
```

## Troubleshooting

### Services Not Starting

**Check if containers are running:**
```bash
podman-compose ps
```

**View logs for a specific service:**
```bash
podman-compose logs mysql
podman-compose logs elasticsearch
podman-compose logs app
```

### Port Already in Use

If you see errors about ports 3306, 9200, or 8080 being in use:

1. Find what's using the port:
```bash
lsof -i :3306
lsof -i :9200
lsof -i :8080
```

2. Either stop the conflicting service or change ports in `podman-compose.yml`

### Application Can't Connect to MySQL

**Verify MySQL is healthy:**
```bash
podman exec spring-cloud-mysql mysqladmin ping -h localhost -u springuser -pspringpass
```

**Check MySQL logs:**
```bash
podman logs spring-cloud-mysql
```

### Application Can't Connect to Elasticsearch

**Check Elasticsearch health:**
```bash
curl http://localhost:9200/_cluster/health?pretty
```

**Check Elasticsearch logs:**
```bash
podman logs spring-cloud-elasticsearch
```

### Podman Machine Not Running (macOS/Windows)

```bash
podman machine start
```

### Clean Restart

If everything seems broken, do a clean restart:

```bash
# Stop and remove all containers and volumes
podman-compose down -v

# Remove any orphaned images (optional)
podman image prune -a

# Rebuild and restart
mvn clean package -DskipTests
podman-compose up -d --build
```

## Key Commands Reference

### Application Management
```bash
./local-dev.sh start           # Start everything
./local-dev.sh stop            # Stop all services
./local-dev.sh test            # Test the application
./local-dev.sh                 # Interactive menu
```

### Container Management
```bash
podman-compose up -d           # Start all services in background
podman-compose down            # Stop all services
podman-compose ps              # List running containers
podman-compose logs -f         # Follow logs from all services
podman-compose restart app     # Restart the application container
```

### Development Workflow
```bash
# Make code changes, then:
mvn clean package -DskipTests  # Rebuild
podman-compose restart app     # Restart app container
podman-compose logs -f app     # View application logs
```

### Database Access
```bash
# MySQL CLI
podman exec -it spring-cloud-mysql mysql -u springuser -pspringpass springcloud

# Check Elasticsearch
curl http://localhost:9200/_cat/indices?v
curl http://localhost:9200/request-logs/_search?pretty
```

## Service URLs

When running locally, the services are available at:

- **Application**: http://localhost:8080
- **MySQL**: localhost:3306
  - Database: `springcloud`
  - Username: `springuser`
  - Password: `springpass`
- **Elasticsearch**: http://localhost:9200

## Running Only Infrastructure (for Development)

If you want to run the app from your IDE while using containerized database and Elasticsearch:

```bash
# Start only MySQL and Elasticsearch
podman-compose up -d mysql elasticsearch

# Run the app from your IDE or command line
mvn spring-boot:run -Dspring-boot.run.profiles=local

# When done, stop infrastructure
podman-compose stop mysql elasticsearch
```

## Additional Resources

For more detailed information, see:

- **[QUICKSTART.md](QUICKSTART.md)** - Quick start guide with more options
- **[LOCAL_DEVELOPMENT.md](LOCAL_DEVELOPMENT.md)** - Comprehensive local development guide
- **[README.md](README.md)** - Full project documentation and AWS Lambda deployment

## Common Issues and Solutions

### 1. Maven Build Fails
```bash
# Clear Maven cache and rebuild
rm -rf ~/.m2/repository/com/example/aws-lambda-spring-boot
mvn clean install -U
```

### 2. Out of Disk Space
```bash
# Clean up unused Podman images and containers
podman system prune -a -f
```

### 3. Changes Not Reflecting
```bash
# Rebuild with --build flag
mvn clean package -DskipTests
podman-compose up -d --build app
```

### 4. Need to Reset Database
```bash
# Stop and remove volumes, then restart
podman-compose down -v
podman-compose up -d
```

## Quick Reference: Test Workflow

```bash
# 1. Start the application
./local-dev.sh start

# 2. Test with curl
curl -X POST http://localhost:8080/processRequest \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","message":"Hello World"}'

# 3. Verify in MySQL
podman exec -it spring-cloud-mysql mysql -u springuser -pspringpass springcloud -e "SELECT * FROM requests;"

# 4. Verify in Elasticsearch
curl http://localhost:9200/request-logs/_search?pretty

# 5. Run tests
mvn test

# 6. Stop when done
./local-dev.sh stop
```

---

**Note:** This application is designed to run both locally (with Podman/Docker) and as an AWS Lambda function. This guide focuses on local development and testing using Podman.

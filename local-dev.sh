#!/bin/bash

# Spring Cloud Function Local Development Helper Script

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Check if podman is installed
check_podman() {
    if command -v podman &> /dev/null; then
        print_success "Podman is installed"
        return 0
    else
        print_error "Podman is not installed"
        echo "Please install Podman from https://podman.io/getting-started/installation"
        return 1
    fi
}

# Check if podman-compose is installed
check_podman_compose() {
    if command -v podman-compose &> /dev/null; then
        print_success "podman-compose is installed"
        return 0
    else
        print_error "podman-compose is not installed"
        echo "Install with: pip3 install podman-compose"
        return 1
    fi
}

# Build the application
build_app() {
    print_info "Building application with Maven..."
    mvn clean package -DskipTests
    print_success "Application built successfully"
}

# Start all services
start_services() {
    print_info "Starting services with podman-compose..."
    podman-compose up -d
    
    print_info "Waiting for services to be ready..."
    sleep 10
    
    # Check MySQL
    if podman exec spring-cloud-mysql mysqladmin ping -h localhost -u springuser -pspringpass &> /dev/null; then
        print_success "MySQL is ready"
    else
        print_error "MySQL is not ready"
    fi
    
    # Check Elasticsearch
    if curl -s http://localhost:9200/_cluster/health &> /dev/null; then
        print_success "Elasticsearch is ready"
    else
        print_error "Elasticsearch is not ready (may take a few more seconds)"
    fi
    
    print_success "All services started"
    echo ""
    print_info "Service URLs:"
    echo "  - Application: http://localhost:8080"
    echo "  - MySQL: localhost:3306"
    echo "  - Elasticsearch: http://localhost:9200"
}

# Stop all services
stop_services() {
    print_info "Stopping services..."
    podman-compose down
    print_success "All services stopped"
}

# View logs
view_logs() {
    print_info "Viewing logs (Ctrl+C to exit)..."
    podman-compose logs -f
}

# Test the application
test_app() {
    print_info "Testing application..."
    
    response=$(curl -s -X POST http://localhost:8080/processRequest \
        -H "Content-Type: application/json" \
        -d '{"name":"Test User","message":"Hello from local dev"}')
    
    if [ $? -eq 0 ]; then
        print_success "Application responded successfully"
        echo "Response: $response"
    else
        print_error "Application test failed"
    fi
}

# Main menu
show_menu() {
    echo ""
    echo "Spring Cloud Function - Local Development"
    echo "=========================================="
    echo "1. Check prerequisites"
    echo "2. Build application"
    echo "3. Start all services (full stack)"
    echo "4. Start infrastructure only (MySQL + Elasticsearch)"
    echo "5. Stop services"
    echo "6. View logs"
    echo "7. Test application"
    echo "8. Clean restart (remove volumes)"
    echo "9. Exit"
    echo ""
    read -p "Select option: " choice
}

# Process menu choice
process_choice() {
    case $choice in
        1)
            check_podman
            check_podman_compose
            ;;
        2)
            build_app
            ;;
        3)
            build_app
            start_services
            ;;
        4)
            print_info "Starting infrastructure services only..."
            podman-compose up -d mysql elasticsearch
            print_success "Infrastructure services started"
            echo ""
            print_info "Run the app from your IDE with profile: local"
            echo "Or run: mvn spring-boot:run -Dspring-boot.run.profiles=local"
            ;;
        5)
            stop_services
            ;;
        6)
            view_logs
            ;;
        7)
            test_app
            ;;
        8)
            print_info "Cleaning and restarting..."
            podman-compose down -v
            build_app
            start_services
            ;;
        9)
            print_info "Goodbye!"
            exit 0
            ;;
        *)
            print_error "Invalid option"
            ;;
    esac
}

# Main loop
main() {
    if [ "$1" = "start" ]; then
        check_podman || exit 1
        check_podman_compose || exit 1
        build_app
        start_services
    elif [ "$1" = "stop" ]; then
        stop_services
    elif [ "$1" = "test" ]; then
        test_app
    else
        while true; do
            show_menu
            process_choice
        done
    fi
}

main "$@"

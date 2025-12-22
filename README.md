This repository contains a **full E-commerce Microservices Proof of Concept (POC)** built using Java and Spring Boot. The project demonstrates cloud-native microservices, service discovery, API gateway routing, containerization, messaging, and observability.


## Repository Modules

|Module|Description|
|---|---|
|configserver|Spring Cloud Config Server providing centralized configuration management to all services.|
|eurekaserver|Eureka Server for service discovery, allowing services to register and discover each other.|
|gateway|API Gateway using Spring Cloud Gateway to route client requests to respective microservices.|
|user|User Service for managing authentication and user-related data.|
|product|Product Service for catalog and inventory management.|
|order|Order Service handling order creation, updates, and tracking.|
|additional|Additional utilities, e.g., MinIO for file storage.|
|docker-compose.yml|Docker Compose setup for container orchestration and service deployment.|
|.gitignore|Prevent tracking of IDE and system-specific files.|
|.idea|IDE configuration (not tracked by git).|
|.DS_Store|macOS file system metadata (ignored in tracking).|



## Architecture Overview

The system follows a **microservices architecture**:

- All services register with **Eureka Server** for service discovery.
    
- **Config Server** provides centralized configuration to all services.
    
- **API Gateway** routes requests from clients (web/mobile) to the appropriate service.
    
- Services communicate via REST APIs; asynchronous messaging is supported with **RabbitMQ**.
    
- Docker is used for containerization of all services.
    
- Additional services include MinIO for storage and monitoring tools for observability.
    

### Observability & Monitoring

- **Logging:** Grafana Loki collects logs from all services.
    
- **Metrics:** Prometheus monitors performance and health.
    
- **Distributed Tracing:** Zipkin traces requests across microservices.
    

---

## Technologies Used

- Java, Spring Boot, Spring Cloud
    
- Docker, Docker Compose
    
- Netflix Eureka, Spring Cloud Config Server
    
- RabbitMQ for messaging
    
- MySQL / Redis databases
    
- Prometheus, Grafana Loki, Zipkin for observability
    
- Maven/Gradle build tools
    
- Git for version control
    


## Illustration
![E-commerce Microservices Architecture](https://i.imgur.com/gtwZtqr.png)
## Setup & Deployment

1. Clone the repository:
    

```bash
git clone https://github.com/DenisGithuku/ecommerce-microservice-poc.git
cd ecommerce-microservice-poc
```

2. Configure application properties for each service (database, RabbitMQ, config server URLs).
    
3. Build the services:
    

```bash
mvn clean install
```

4. Start services using Docker Compose:
    

```bash
docker-compose up --build
```

5. Access the API Gateway at `http://localhost:8080`.
    

---

## Features

- Microservices-based architecture with independent deployment
    
- Centralized configuration and service discovery
    
- REST API communication with optional asynchronous messaging
    
- Fault tolerance and resilience
    
- Observability via logging, metrics, and tracing
    

---

## Contact

- GitHub: [DenisGithuku](https://github.com/DenisGithuku)
    
- Email: [githukudenis@icloud.com](mailto:githukudenis@icloud.com)
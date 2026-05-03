# Grafana And Prometheus Setup

This project now includes Prometheus and Grafana support without changing the existing business logic classes.

## What Was Added

- Spring Boot Actuator dependency
- Prometheus Micrometer registry dependency
- actuator metrics properties in `application.properties`
- Prometheus scrape config
- Grafana datasource provisioning
- Grafana dashboard provisioning
- Docker Compose file for Prometheus and Grafana

## URLs

When the Spring Boot app is running:

- Actuator health: `http://localhost:8080/actuator/health`
- Prometheus metrics: `http://localhost:8080/actuator/prometheus`

When monitoring stack is running:

- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000`

Grafana default login:

- username: `admin`
- password: `admin`

## How To Run

1. Start the Spring Boot application
2. Start monitoring stack:

```powershell
docker compose -f docker-compose.monitoring.yml up -d
```

3. Open Grafana and view the auto-provisioned `HyperStream Overview` dashboard

## Important Note

The Prometheus config uses:

`host.docker.internal:8080`

This works well when Docker is running on Windows and your Spring Boot app is running on the host machine.

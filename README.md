# HyperStream

Design and implement a scalable, high-performance backend system for a streaming platform that efficiently handles CRUD operations, reduces latency using caching, supports asynchronous processing, and provides real-time monitoring and reliability under high load conditions.

---

##  Features

- CRUD APIs for Movies, Users, Subscriptions, and Watchlist  
- Redis caching for performance optimization  
- RabbitMQ for asynchronous processing  
- PostgreSQL for data storage  
- Layered architecture (Controller → Service → Repository → Model)  
- Integrated frontend (Netflix-style UI)  
- Monitoring using Prometheus and Grafana  
- Rate limiting for API protection  

---

## Tech Stack

- Spring Boot (Java 17)  
- PostgreSQL  
- Redis  
- RabbitMQ  
- Prometheus & Grafana  
- Nginx  
- HikariCP  

---

## Modules

- Movie Management  
- User Management  
- Subscription Management  
- Watchlist Management  

---

## Key Highlights

- Improved performance using caching  
- Non-blocking system using async messaging  
- Scalable architecture for high traffic  
- Real-time monitoring and observability  

---

##  Performance

- ~120 ms average response time  
- ~380 requests/sec throughput  
- <0.5% error rate  

---

##  Workflow

1. Request hits controller  
2. Service processes logic  
3. Data fetched from DB / Cache  
4. Async events handled via RabbitMQ  
5. Metrics monitored via Prometheus & Grafana  

---

##  Authors

- Juhi Hirpara  
- Parthiv Dhameliya  

---

⭐ If you like this project, give it a star!

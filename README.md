# 🍕 Food Delivery Service

---

A **microservices backend** for a food delivery platform, built with Java 21 and Spring Boot 3.
Demonstrates event-driven architecture with Kafka, synchronous REST communication,
and a shared DTO library — designed with real-world engineering practices in mind.
---

## Architecture Overview

```
┌───────────────┐    ┌─────────────────┐        HTTP (REST)        ┌──────────────────┐
|               |    │  Order Service  │  ───────────────────────► │ Payment Service  │
|     Client    |───►│                 │                           │                  │
|               |    │  PostgreSQL DB  │                           │  PostgreSQL DB   │
└───────────────┘    └────────┬────────┘                           └──────────────────┘
                              │
                              │  Apache Kafka
                              │  (async event stream)
                              ▼
                     ┌─────────────────┐
                     │Delivery Service │
                     │                 │
                     │  PostgreSQL DB  │
                     └─────────────────┘
```

> **Planned:** Replacing REST communication between Order and Payment services with **gRPC** for lower latency and strongly-typed contracts.

---

## Services

### 🧾 Order Service
Handles the full order lifecycle — from cart submission to fulfillment tracking.
- Exposes REST API for order creation and status management
- Publishes domain events to Kafka upon order confirmation
- Communicates synchronously with Payment Service via HTTP
- Consumer Kafka events from Delivery Service asynchronously

### 💳 Payment Service
Processes payment requests triggered by the Order Service.
- Validates and processes payments via REST endpoint
- Maintains its own isolated payment records and transaction history

### 🚗 Delivery Service
Listens for confirmed orders and manages delivery dispatching.
- Consumes Kafka events from Order Service asynchronously
- Tracks delivery status independently of the order lifecycle
- Publishes events to Kafka about assigned delivery to orders

---

## Tech Stack

| Category | Technology                                 |
|---|--------------------------------------------|
| Language | Java 21                                    |
| Framework | Spring Boot 3, Spring MVC, Spring Data JPA |
| Messaging | Apache Kafka                               |
| Database | PostgreSQL (per service)                   |
| ORM | Hibernate                                  |
| Shared Contracts | common-libs (internal Maven module)        |
| Containerization | Docker, Docker Compose                     |
| Build Tool | Maven                                      |

---

## Key Design Decisions

**Database per Service** — Each service has its own PostgreSQL instance.
No shared schema, load is distributed across 3 separate databases,
each scalable independently.

**Shared DTO Library** — A standalone `common-libs` Maven module holds all shared DTOs and enums (e.g. `OrderDTO`, `OrderStatus`, `CreatePaymentRequestDTO`).
This eliminates duplication while keeping services independently deployable.

**Kafka for Order ↔ Delivery** — Order service publishes `OrderPaidEvent` event once payment
is confirmed. Delivery service picks it up, assigns a courier, then publishes
`DeliveryAssignedEvent` back. Order service consumes it and updates
`orderStatus`, `etaMinutes` and `courierName`.

**Sync HTTP for Payments** — Payment confirmation is part of the order flow and
requires an immediate response, making synchronous HTTP the right choice here.
(gRPC migration planned.)

---

## Getting Started

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven 3.8+

### Run the full stack

```bash
git clone https://github.com/edelBrock89t/food-delivery-system.git
cd food-delivery-service

# Start infrastructure (PostgreSQL instances + Kafka)
docker-compose up -d

# Install shared library
cd common-libs && mvn install && cd ..

# Start each service
cd order-service && mvn spring-boot:run &
cd payment-service && mvn spring-boot:run &
cd delivery-service && mvn spring-boot:run
```

---

## Project Structure

```
food-delivery-service/
├── common-libs/                  # Shared DTOs and enums
├── order-service/                # Order lifecycle management
├── payment-service/              # Payment processing
├── delivery-service/             # Delivery dispatching
└── docker-compose.yml            # Full local infrastructure
```

---

## Roadmap
- [ ] Replace HTTP (Order ↔ Payment) with **gRPC** secured with **mTLS**
- [ ] Add distributed tracing with **Micrometer + Zipkin**
- [ ] Add **API Gateway** (Spring Cloud Gateway)
- [ ] Add another microservices like **client-service**, **auth-service (with JWT)**, **notification-service**, **warehouse-service**
- [ ] Add Redis caching
- [ ] Kubernetes deployment manifests

---

## Author

**IB**
Java Backend Developer
[LinkedIn](-) · [GitHub](https://github.com/edelBrock89t/)
# StockBridge 360

**Smart Scrap Metal & Recycling Inventory Management System**  
CPAN228 Web Application Development · CircularCode Developers

## Overview
StockBridge 360 is a Spring Boot/Thymeleaf inventory management demo for weight-based scrap-metal operations. The main web application handles materials, warehouses, stock lots, purchasing, sales and role-based user access. A separate Spring Boot alert service provides a REST API and independent database.

## Requirements
- Java JDK **17 or newer** (JDK 21 recommended)
- Apache Maven 3.9+
- Docker Desktop for the QA PostgreSQL profile
- Internet connection on first build for Maven dependencies and Bootstrap CDN

## Quick start — Windows PowerShell
Open **two terminals** at the project root:

**Terminal 1: Alert service (dev/H2)**
```powershell
mvn -pl stockbridge-alert-service spring-boot:run -Dspring-boot.run.profiles=dev
```
**Terminal 2: Web application (persistent H2)**
```powershell
mvn -pl stockbridge-web spring-boot:run
```
Open http://localhost:8080.

**Demo admin account:** `admin@stockbridge.local` / `ChangeMe123!`  
This is for **local demonstration only**. Change or remove this account before any deployment. New registered users receive the STAFF role. An ADMIN can promote them to MANAGER in Administration.

> If PowerShell interprets the Maven profile property unexpectedly, use `mvn -pl stockbridge-alert-service spring-boot:run "-Dspring-boot.run.profiles=dev"`.

## QA profile with PostgreSQL
1. Start Docker Desktop.
2. From the root run `docker compose up -d`.
3. Start the alert service with:
   `mvn -pl stockbridge-alert-service spring-boot:run "-Dspring-boot.run.profiles=qa"`
4. Start the web app as above.
5. Stop database: `docker compose down` (data persists in Docker volume).

The QA profile uses a separate PostgreSQL database, while the dev profile uses H2 memory. Change example passwords before publishing or deploying.

## Features
- Public home, About, and How it works pages
- Login/logout, BCrypt registration, STAFF/MANAGER/ADMIN roles
- Material catalog with validated form, JPA pagination, name/category filters, name/price/date sorting
- Persistent sample data seeded on first launch (Java initializer)
- Stock lots by yard, supplier and material
- Purchase posting increases inventory and checks warehouse capacity
- Sale posting consumes oldest stock lots first and blocks overselling
- Admin role management and catalog delete action
- Remote alerts via RestTemplate; dashboard stays available when service is down
- Responsive Bootstrap interface

## Architecture
```text
Browser -> Spring MVC / Thymeleaf (port 8080)
             | Spring Security (BCrypt + database users)
             | Spring Data JPA -> H2 file database
             | RestTemplate + Basic Auth
             v
          Alert REST service (port 8081)
             | independent Spring Security user
             | Spring Data JPA
             +-> dev: H2 in-memory
             +-> qa: PostgreSQL in Docker
```

## REST API
HTTP Basic: username `bridge`, password `bridge-dev-password` (development only).

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/alerts` | List alerts |
| GET | `/api/alerts/{id}` | Get one; 404 if absent |
| GET | `/api/alerts/search?severity=WARNING&status=OPEN` | Filter by two parameters |
| POST | `/api/alerts` | Create; 201 |
| PUT | `/api/alerts/{id}` | Update; 404 if absent |
| DELETE | `/api/alerts/{id}` | Delete; 204 or 404 |

POST/PUT JSON example:
```json
{"message":"West Yard nearing capacity","severity":"WARNING","status":"OPEN"}
```

## Suggested demo walkthrough
1. Open home, About and How it works.
2. Sign in as admin; show dashboard and populated inventory.
3. Filter catalog by category and sort by price.
4. Add a material; show validation on an invalid submission.
5. Post a purchase and show stock weight increase.
6. Attempt an oversized sale (rejected); post a valid sale.
7. Register a staff account; show access restrictions and promote via admin.
8. Show alert service, create an alert from admin, and demonstrate graceful offline status.
9. Run the QA profile against Docker PostgreSQL.

## Team contributions
- Insan Mohammed Kamilu — proposed purchase/sale workflows and dashboard integration
- Parmvir Sanghera — proposed catalog/warehouse views and entity mapping

These are **planned responsibilities from Assignment 0**, not claims of completed individual work. Each teammate must make genuine commits and document their actual contributions before submission.

## Known limitations / improvements
This is a substantial **starter implementation**, not a verified production system. Before submission, compile and run both modules, test role restrictions, form validation, CRUD and QA profile, and fix any issues. Sample data is initialized using a Java runner rather than `data.sql` in the web app; the professor specifically requests `data.sql`, so convert that seeding mechanism before grading. The current admin interface supports deleting materials and changing roles, but does not yet implement editing materials, supplier/warehouse CRUD, audit logging, concurrency-safe stock allocation, or comprehensive automated tests. The purchase model currently uses one material per transaction rather than a separate PurchaseLine entity. These are remaining rubric/design gaps, not finished features.

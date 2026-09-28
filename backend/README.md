# Solar Installation Tracker — Backend (Phase 1: Auth + Installation Records + DCR Tracking)

Separate project from the Solar CRM, same stack (Java 17, Spring Boot 3, MySQL, JWT), different database (`solar_tracker`) and port (`8081` by default, so it can run alongside the CRM's `8080`).

## What's included

- User/Role/JWT auth (`SUPER_ADMIN`, `ADMIN`, `INSTALLER`)
- **Module 1 — Installation records**: matches your ULA tracking sheet — ULA Application ID, section, distribution, service number, consumer name/mobile, inverter serial, panel serials (normalized into their own table so a site isn't capped at 4 panels), site type, remarks
- **Module 2 — DCR tracking**: a dedicated `PATCH /api/installations/{id}/dcr-status` action separate from editing the rest of the record, since in practice DCR review is an ADMIN action distinct from data entry
- Duplicate protection: ULA Application ID and service number are unique; a panel serial number can only ever be attached to one installation (this mirrors the real-world MNRE rule against reusing a module's serial across multiple subsidy claims)
- Dummy seed data (3 fictional installations, 2 with panels) — **not** the data from any spreadsheet you've shared

## 1. Prerequisites

Same as the CRM: Java 17+, Maven, MySQL 8.

## 2. Create the database

```bash
mysql -u root -p < database/schema.sql
```

Creates `solar_tracker` with `roles`, `users`, `user_roles`, `installations`, `installation_panels`, seeds the 3 roles, a default login, and 3 dummy installations.

Default login:
```
username: superadmin
password: Admin@123
```

## 3. Run it

```bash
cd backend
mvn spring-boot:run
```

Runs on **http://localhost:8081**. Swagger: **http://localhost:8081/swagger-ui.html**.

If you're running the CRM backend at the same time, its default port (8080) won't conflict with this one (8081).

## 4. Test in Postman

**Login**
```
POST http://localhost:8081/api/auth/login
{ "username": "superadmin", "password": "Admin@123" }
```

**List installations (Module 1)**
```
GET http://localhost:8081/api/installations?page=0&size=20
Authorization: Bearer {{token}}
```
Try filters: `?dcrStatus=PENDING`, `?status=SUBMITTED`, `?search=DEMO-0002`

**Create an installation**
```
POST http://localhost:8081/api/installations
Authorization: Bearer {{token}}
{
  "ulaApplicationId": "ULA-UO-DEMO-0001-0000004",
  "section": "DEMO SECTION",
  "distribution": "01 - DEMO DISTRIBUTION",
  "serviceNumber": "9000000004",
  "consumerName": "Test Consumer Four",
  "consumerMobile": "9000000014",
  "siteType": "Residential",
  "panelSerialNumbers": ["DEMO-PANEL-0004-01", "DEMO-PANEL-0004-02"]
}
```

**Update DCR status (Module 2)**
```
PATCH http://localhost:8081/api/installations/1/dcr-status
Authorization: Bearer {{token}}
{ "dcrStatus": "UPLOADED", "remarks": "Certificate uploaded 2026-09-27" }
```

**Duplicate panel serial (should 409)**
```
POST http://localhost:8081/api/installations
{ ..., "panelSerialNumbers": ["DEMO-PANEL-0002-01"] }   // already used by installation #2
```
Expected: `409 Conflict`, `"errorCode": "DUPLICATE_RESOURCE"`.

## 5. Next

Angular frontend for these two modules (list/filter/create/edit for installations, a DCR status action), then the remaining phases (photos, geolocation, offline sync, admin dashboard, service tickets, reports) once these two are confirmed working for you.

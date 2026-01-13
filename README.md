# Clinic Management System

## Overview
This project is a **Spring Boot backend application** for managing a medical clinic.  
It covers the complete workflow of a clinic: patients, clinics, doctors, schedules, appointments, medical records, prescriptions, security with roles, and audit logging.

The application is designed as an **MVP-ready system**, following clean architecture principles:
- REST APIs  
- Business logic separated in services  
- Persistence with JPA + Flyway  
- Security with JWT and roles  
- Full API documentation via Swagger  
- Demonstration via Postman  

---

## Business Domain
Medical clinic management:
- Patient registration  
- Doctor and clinic administration  
- Doctor schedules and available slots  
- Appointment booking and lifecycle  
- Medical records and prescriptions  
- Role-based access control  
- Audit and traceability  

---

## Business Requirements
The system fulfills the following high-level requirements:
- Unique patient registration (email/CNP)  
- Doctors assigned to clinics  
- Doctor working schedules and availability  
- Appointment creation with conflict validation  
- Appointment lifecycle (scheduled, cancelled, completed)  
- Medical records linked to appointments  
- Prescriptions linked to medical records  
- Data integrity through logical deactivation  
- Security with roles (ADMIN / DOCTOR / RECEPTIONIST)  
- Full audit logging of critical actions  

---

## MVP Features

### F1. Patient Management
CRUD operations for patients, uniqueness validation, logical deactivation.

### F2. Clinic & Doctor Management
CRUD clinics and doctors, specialization, active/inactive status.

### F3. Doctor Schedule & Available Slots
Define doctor working hours and calculate available slots dynamically.

### F4. Appointment Management
Create, cancel, complete appointments with overlap and schedule validation.

### F5. Medical Records, Prescriptions, Security & Audit
Medical records and prescriptions, role-based access, ownership rules, audit logging.

---

## Architecture
Layered architecture:
- Controller (REST layer)  
- Service (business logic)  
- Repository (data access)  
- Entity (JPA models)  

Database migrations are handled via **Flyway**.  
The schema is validated at startup.

---

## Technologies Used
- Java 21+  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- Spring Security (JWT, OAuth2 Resource Server)  
- PostgreSQL  
- Flyway  
- Swagger / OpenAPI  
- Postman  

---

## Database Model

### Persisted Entities
- Patient  
- Clinic  
- Doctor  
- DoctorSchedule  
- Appointment  
- MedicalRecord  
- Prescription  
- UserAccount  
- AuditLog  

### Relationships
- Clinic → Doctors  
- Doctor → Schedules  
- Doctor → Appointments  
- Patient → Appointments  
- Appointment → MedicalRecord  
- MedicalRecord → Prescriptions  
- UserAccount → Doctor  

---

## Security
Authentication is based on **JWT**.  
Authorization is enforced via roles:

- ADMIN  
- DOCTOR  
- RECEPTIONIST  

### Access Rules
- ADMIN: full access, audit access  
- DOCTOR: own medical records and prescriptions only  
- RECEPTIONIST: scheduling and available slots access  

---

## Audit Logging
All critical actions are logged:
- Entity type  
- Entity ID  
- Action  
- Actor email  
- Actor roles  
- Timestamp  
- Details  

Admins can query audit logs using filters:
- entityType  
- entityId  
- action  
- dateFrom / dateTo  

---

## API Documentation
All REST endpoints are documented using **Swagger / OpenAPI**.  
The documentation is available through the application and can be exported as OpenAPI JSON or YAML and committed to the repository.

---

## Testing

The application is tested and demonstrated using **Postman**.

### Testing Strategy
- Manual API testing via Postman collections  
- Validation of business rules (overlapping appointments, schedule constraints)  
- Validation of role-based access control  
- End-to-end flow testing  

### Covered Scenarios
- User registration and authentication  
- Role-based authorization (ADMIN / DOCTOR / RECEPTIONIST)  
- Patient creation and deactivation  
- Doctor and schedule setup  
- Available slots computation  
- Appointment creation, cancellation, completion  
- Medical record and prescription management  
- Audit log generation and querying  

---

## Notes
- Physical deletion is avoided for critical entities; logical deactivation is used instead  
- Hibernate schema validation is enabled (`ddl-auto=validate`)  
- Database schema evolution is handled exclusively through Flyway migrations  
- The project structure allows easy extension with new features  

---

## Conclusion
This project delivers a complete, secure, and auditable clinic management backend.  
It fully satisfies the academic requirements while following professional backend development practices, and it can be easily extended into a production-ready system.

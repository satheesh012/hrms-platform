# 🏢 HRMS Microservices Platform

[![CI Pipeline](https://img.shields.io/badge/CI-GitHub_Actions-blue?logo=github-actions)](https://github.com/satheesh012/hrms-platform/actions)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.2-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17_Temurin-orange?logo=openjdk)](https://adoptium.net/)
[![Docker](https://img.shields.io/badge/Docker-Multi--Stage-2496ED?logo=docker)](https://www.docker.com/)
[![Security](https://img.shields.io/badge/Security-Aqua_Trivy-blueviolet?logo=aqua)](https://trivy.dev/)

An enterprise-grade, cloud-native **Human Resource Management System (HRMS)** built with Java 17, Spring Boot, Spring Cloud, and a modern **DevSecOps** delivery pipeline.

---

## 🏗️ Architecture Overview

The platform is designed as a distributed, polyglot-ready microservices architecture utilizing Netflix Eureka for service registry, Spring Cloud Gateway for centralized API routing, and dedicated microservices for distinct domain operations.

```mermaid
graph TD
    Client["Client / Web App"] --> Ingress["Ingress-NGINX (myapp.local)"]
    Ingress --> Gateway["API-Gateway (Port 8080)"]

    Gateway --> Registry["Service-Registry / Eureka (Port 8761)"]
    Gateway --> Auth["Auth-Service (Port 8081)"]
    Gateway --> Dept["Department-Service (Port 8083)"]
    Gateway --> Emp["Employee-Service (Port 8082)"]
    Gateway --> Att["Attendance-Service (Port 8084)"]
    Gateway --> Leave["Leave-Service (Port 8085)"]
    Gateway --> Pay["Payroll-Service (Port 8086)"]
    Gateway --> Notif["Notification-Service (Port 8087)"]
    Gateway --> Sched["Scheduler-Service (Port 8088)"]

    Auth --> DB[(MySQL Databases)]
    Dept --> DB
    Emp --> DB
    Att --> DB
    Leave --> DB
    Pay --> DB
```

---

## 📦 Microservices Ecosystem

| Microservice | Port | Description | Database / Storage |
| :--- | :---: | :--- | :--- |
| **`Service-Registry`** | `8761` | Eureka Server for service discovery and heartbeat health registration. | In-Memory Registry |
| **`API-Gateway`** | `8080` | Spring Cloud Gateway for reverse proxying, authentication filtering, and routing. | Stateless |
| **`Auth-Service`** | `8081` | Authentication & authorization engine generating and validating JWT tokens. | MySQL (`authService`) |
| **`Department-Service`** | `8083` | Department hierarchies, managerial associations, and organizational units. | MySQL (`departmentService`) |
| **`Employee-Service`** | `8082` | Employee master profiles, roles, and historical records. Features Kubernetes HPA. | MySQL (`employeeService`) |
| **`Attendance-Service`**| `8084` | Clock-in/clock-out tracking, daily timesheet logs, and attendance summaries. | MySQL (`attendanceService`) |
| **`Leave-Service`** | `8085` | Leave requests, balance tracking, and managerial approval workflows. | MySQL (`leaveService`) |
| **`Payroll-Service`** | `8086` | Salary computations, deductions, tax calculations, and payslip generation. | MySQL (`payrollService`) |
| **`Notification-Service`**| `8087` | Asynchronous email and message notifications with Gmail SMTP integration. | Stateless |
| **`Scheduler-Service`** | `8088` | Automated cron triggers for scheduled reminders, payroll runs, and reports. | Stateless |

---

## 🔒 DevSecOps & Containerization

### 1. Multi-Stage Alpine Builds
All services leverage Docker multi-stage builds (`maven:3.9-eclipse-temurin-17-alpine` ➡️ `eclipse-temurin:17-jre-alpine`):
* **Slim Image Size:** Reduced final image footprint from ~800MB to ~220MB (a 72% reduction).
* **Minimal Attack Surface:** Builder tools, source code, and compilers are stripped from production runtime images.

### 2. Non-Root Security Context
Images execute under a dedicated non-root user:
```dockerfile
RUN addgroup -S spring && adduser -S spring -G spring -u 1000
USER spring:spring
```
This adheres to Kubernetes pod security standards (`runAsNonRoot: true`, `runAsUser: 1000`).

### 3. Vulnerability Scanning (Aqua Trivy)
Integrated directly into the CI pipeline to scan container images for OS and library CVEs (`CRITICAL,HIGH`) before pushing to Docker Hub.

---

## ⚡ Smart CI/CD Pipeline (GitHub Actions)

The Monorepo CI pipeline in [`.github/workflows/ci.yml`](.github/workflows/ci.yml) utilizes **Path Filtering** (`dorny/paths-filter@v3`):

* **Selective Compilation & Image Build:** Automatically evaluates `git diff` against the target branch ref. Only services whose directory or configuration changed will trigger Maven compilation, Docker packaging, Trivy scans, and registry pushes.
* **Drastic Build-Time Reduction:** Unchanged pushes finish in **~20 seconds**, saving hundreds of GitHub Actions runner minutes compared to full 10-service rebuilds (~5 minutes).
* **Automated GitOps Tag Propagation:** On successful build, the workflow updates the respective image tag inside the Kubernetes configuration repository (`hrms-k8s`) and commits automatically.

```mermaid
graph LR
    Push["git push origin dev/main"] --> Filter["Path-Filter Step<br/>(dorny/paths-filter@v3)"]
    Filter -- "Files changed in Service X" --> Build["Maven Package + Docker Build (Service X only)"]
    Filter -- "Untouched Services" --> Skip["Skipped (0s runtime)"]
    Build --> Scan["Aqua Trivy Security Scan"]
    Scan --> PushImage["Docker Hub Push (:SHA)"]
    PushImage --> GitOpsCommit["Auto-Commit Tag to hrms-k8s"]
```

---

## 💻 Local Development

### Prerequisites
* JDK 17 (Eclipse Temurin recommended)
* Maven 3.9+
* Docker Desktop & Docker Compose (or Kubernetes)

### Build All Services
```bash
mvn clean package -DskipTests
```

### Build a Single Microservice
```bash
mvn -f Department-Service/pom.xml clean package -DskipTests
```

### Build Docker Image Manually
```bash
docker build -t satheesh012/department-service:latest -f Department-Service/Dockerfile .
```

---

## 🔗 Related Repositories

* **[hrms-k8s](https://github.com/satheesh012/hrms-k8s):** Kubernetes manifests, ArgoCD GitOps configuration, and Universal Helm Chart.

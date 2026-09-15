# ==========================================
# Stage 1: Build JAR inside container (Builder)
# ==========================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# 1. Copy parent POM and service POM for dependency resolution
COPY pom.xml ./
COPY Department-Service/pom.xml ./Department-Service/

# 2. Copy source code of Department-Service
COPY Department-Service/src ./Department-Service/src

# 3. Package the service
RUN mvn -f Department-Service/pom.xml clean package -DskipTests

# ==========================================
# Stage 2: Minimal Runtime Image (Runner)
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create system group & user with explicit UID/GID 1000 for Alpine
RUN addgroup -g 1000 -S spring && adduser -u 1000 -S spring -G spring

# Copy ONLY the compiled .jar from the builder stage
COPY --from=builder /build/Department-Service/target/*.jar app.jar

# Set ownership to spring user
RUN chown -R spring:spring /app

# Run as non-root user (matches Kubernetes securityContext.runAsUser: 1000)
USER 1000

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
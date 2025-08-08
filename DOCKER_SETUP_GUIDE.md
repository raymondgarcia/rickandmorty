# Rick and Morty - Hexagonal Architecture
## 🚀 Docker & SonarQube Setup Guide

### 📋 Servicios Incluidos

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **Rick and Morty API** | 8080 | Aplicación principal Spring Boot 3 |
| **PostgreSQL** | 5432 | Base de datos principal |
| **SonarQube** | 9000 | Análisis de calidad de código |
| **SonarQube DB** | - | PostgreSQL para SonarQube |
| **Redis** | 6379 | Cache (opcional) |

### 🛠️ Comandos de Operación

#### Levantar todo el entorno
```bash
# Construir y levantar todos los servicios
docker compose up -d --build

# Ver logs en tiempo real
docker compose logs -f rickandmorty-app

# Ver estado de servicios
docker compose ps
```

#### Gestión individual de servicios
```bash
# Solo base de datos
docker compose up -d rickandmorty-postgres

# Solo SonarQube
docker compose up -d sonarqube sonarqube-postgres

# Solo la aplicación (requiere BD)
docker compose up -d rickandmorty-app
```

#### Comandos de desarrollo
```bash
# Rebuild solo la aplicación
docker compose build rickandmorty-app
docker compose up -d rickandmorty-app

# Ejecutar tests con Maven
./mvnw test

# Generar reporte de cobertura
./mvnw clean test jacoco:report

# Análisis con SonarQube
./mvnw sonar:sonar -Dsonar.host.url=http://localhost:9000
```

### 🔧 Configuración SonarQube

1. **Acceder a SonarQube**: http://localhost:9000
   - Usuario: `admin`
   - Password: `admin` (cambiar en primer acceso)

2. **Crear token de proyecto**:
   ```bash
   # Ejemplo de comando con token
   ./mvnw sonar:sonar \
     -Dsonar.host.url=http://localhost:9000 \
     -Dsonar.login=TU_TOKEN_AQUI
   ```

### 🌐 URLs de Acceso

- **API Swagger**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **SonarQube**: http://localhost:9000

### 📊 Ejemplos de API

```bash
# Obtener personaje por ID
curl http://localhost:8080/api/v1/character/1

# Health check
curl http://localhost:8080/actuator/health

# Metrics (Prometheus format)
curl http://localhost:8080/actuator/prometheus
```

### 🛑 Comandos de Limpieza

```bash
# Parar todos los servicios
docker compose down

# Eliminar volúmenes (⚠️ elimina datos)
docker compose down -v

# Limpiar imágenes
docker system prune -f
```

### 🔍 Troubleshooting

#### Problema: Puerto ocupado
```bash
# Verificar qué usa el puerto
lsof -i :8080
lsof -i :5432
lsof -i :9000

# Cambiar puertos en docker-compose.yml si es necesario
```

#### Problema: Memoria insuficiente para SonarQube
```bash
# Aumentar memoria Docker Desktop
# O desactivar SonarQube temporalmente:
docker compose up -d rickandmorty-postgres rickandmorty-app
```

#### Logs de debugging
```bash
# Ver logs específicos
docker compose logs rickandmorty-app
docker compose logs rickandmorty-postgres
docker compose logs sonarqube

# Entrar al contenedor
docker exec -it rickandmorty-app sh
docker exec -it rickandmorty-postgres psql -U postgres -d rickandmorty
```

### 🏗️ Arquitectura Hexagonal - Estructura

```
src/main/java/cl/mobdev/rm/
├── domain/                 # 🟢 Capa de Dominio (Pura)
│   ├── model/             # Records: Character, Location
│   ├── ports/             # Interfaces: UseCase, Repository
│   └── exception/         # Excepciones de dominio
├── application/           # 🟡 Capa de Aplicación
│   ├── service/          # Orquestadores: CharacterService
│   └── dto/              # DTOs internos
└── infrastructure/       # 🔵 Capa de Infraestructura
    ├── adapter/
    │   ├── inbound/      # REST Controllers
    │   └── outbound/     # JPA, HTTP Clients
    ├── dto/              # DTOs externos
    └── mapper/           # Mappers DTO ↔ Domain
```

### 📈 Métricas y Monitoreo

- **Actuator**: Expone métricas, health checks y endpoints de gestión
- **Micrometer**: Métricas en formato Prometheus
- **JaCoCo**: Cobertura de tests
- **SonarQube**: Análisis estático, vulnerabilidades, code smells

### 🎯 Próximos Pasos

1. **Refactorización hexagonal completa** según `HEXAGONAL_ARCHITECTURE_ANALYSIS_REPORT`
2. **Implementar Value Objects** (`CharacterId`, `Species`)
3. **Separar casos de uso** específicos
4. **Añadir tests de slice** con Testcontainers
5. **Circuit breaker** para API externa (Resilience4j)

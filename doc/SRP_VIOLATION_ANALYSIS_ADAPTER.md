# Análisis SRP: RickAndMortyApiClientAdapter

## Violaciones Identificadas

### 1. **Múltiples Responsabilidades en una Clase**
El adapter actual tiene **5 responsabilidades distintas**:

1. **Comunicación HTTP** - Llamadas REST
2. **Lógica de negocio** - Decidir si buscar location
3. **Parsing de URLs** - Extraer IDs de URLs
4. **Transformación** - DTO → Domain
5. **Orquestación** - Coordinar múltiples llamadas

### 2. **Método Demasiado Largo y Complejo**
- 28 líneas en un solo método
- Lógica anidada con `map().orElse()`
- Múltiples niveles de abstracción

### 3. **Inyección de Campo (Anti-patrón)**
```java
@Autowired
RestClient restClient;  // ❌ Debería ser constructor injection
```

## Refactorización Propuesta

### Paso 1: Crear Value Objects y Servicios Especializados

```java
// domain/model/CharacterId.java
package cl.mobdev.rm.domain.model;

public record CharacterId(Integer value) {
    public CharacterId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Character ID must be positive");
        }
    }

    public static CharacterId fromString(String id) {
        try {
            return new CharacterId(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid character ID format: " + id);
        }
    }
}

// domain/model/LocationId.java
package cl.mobdev.rm.domain.model;

public record LocationId(Integer value) {
    public LocationId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Location ID must be positive");
        }
    }
}
```

### Paso 2: Crear Puerto de Dominio Correcto

```java
// domain/ports/out/CharacterRepository.java
package cl.mobdev.rm.domain.ports.out;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.CharacterId;

@FunctionalInterface
public interface CharacterRepository {
    Character findById(CharacterId id);
}

// domain/ports/out/LocationRepository.java
package cl.mobdev.rm.domain.ports.out;

import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.domain.model.LocationId;

@FunctionalInterface
public interface LocationRepository {
    Location findById(LocationId id);
}
```

### Paso 3: Separar Responsabilidades en Clases Específicas

#### A. Servicio de Parsing de URLs (Una Responsabilidad)
```java
// infrastructure/service/RickAndMortyUrlParser.java
package cl.mobdev.rm.infrastructure.service;

import cl.mobdev.rm.domain.model.LocationId;
import org.springframework.stereotype.Component;

@Component
public class RickAndMortyUrlParser {

    // ✅ UNA SOLA RESPONSABILIDAD: Parsing de URLs
    public LocationId extractLocationId(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or blank");
        }

        try {
            String idString = url.substring(url.lastIndexOf("/") + 1);
            Integer id = Integer.parseInt(idString);
            return new LocationId(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid location URL format: " + url, e);
        }
    }
}
```

#### B. Cliente HTTP Especializado (Una Responsabilidad)
```java
// infrastructure/client/RickAndMortyHttpClient.java
package cl.mobdev.rm.infrastructure.client;

import cl.mobdev.rm.domain.model.CharacterId;
import cl.mobdev.rm.domain.model.LocationId;
import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RickAndMortyHttpClient {

    private final RestClient restClient;

    // ✅ Constructor injection
    public RickAndMortyHttpClient(RestClient restClient) {
        this.restClient = restClient;
    }

    // ✅ UNA SOLA RESPONSABILIDAD: HTTP calls
    public CharacterApiDto fetchCharacter(CharacterId id) {
        return restClient.get()
            .uri("character/{id}", id.value())
            .retrieve()
            .body(CharacterApiDto.class);
    }

    public LocationApiDto fetchLocation(LocationId id) {
        return restClient.get()
            .uri("location/{id}", id.value())
            .retrieve()
            .body(LocationApiDto.class);
    }
}
```

#### C. Mapper Especializado (Una Responsabilidad)
```java
// infrastructure/mapper/CharacterDomainMapper.java
package cl.mobdev.rm.infrastructure.mapper;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;

import java.util.Optional;

public final class CharacterDomainMapper {

    private CharacterDomainMapper() {} // Utility class

    // ✅ UNA SOLA RESPONSABILIDAD: Transformación DTO → Domain
    public static Character toDomain(CharacterApiDto dto) {
        Integer episodeCount = dto.episode() != null ? dto.episode().size() : 0;

        return new Character(
            dto.id(),
            dto.name(),
            dto.status(),
            dto.species(),
            dto.type(),
            episodeCount,
            Optional.empty() // Sin location
        );
    }

    public static Character toDomain(CharacterApiDto characterDto, LocationApiDto locationDto) {
        Integer episodeCount = characterDto.episode() != null ? characterDto.episode().size() : 0;

        Optional<Location> location = characterDto.origin()
            .map(origin -> new Location(
                locationDto.name(),
                origin.url(),
                locationDto.dimension(),
                locationDto.residents() != null ? locationDto.residents() : List.of()
            ));

        return new Character(
            characterDto.id(),
            characterDto.name(),
            characterDto.status(),
            characterDto.species(),
            characterDto.type(),
            episodeCount,
            location
        );
    }
}
```

### Paso 4: Adapter Refactorizado (Solo Orquestación)

```java
// infrastructure/adapter/outbound/RickAndMortyCharacterRepositoryAdapter.java
package cl.mobdev.rm.infrastructure.adapter.outbound;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.CharacterId;
import cl.mobdev.rm.domain.model.LocationId;
import cl.mobdev.rm.domain.ports.out.CharacterRepository;
import cl.mobdev.rm.infrastructure.client.RickAndMortyHttpClient;
import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;
import cl.mobdev.rm.infrastructure.mapper.CharacterDomainMapper;
import cl.mobdev.rm.infrastructure.service.RickAndMortyUrlParser;
import org.springframework.stereotype.Component;

@Component
public class RickAndMortyCharacterRepositoryAdapter implements CharacterRepository {

    private final RickAndMortyHttpClient httpClient;
    private final RickAndMortyUrlParser urlParser;

    // ✅ Constructor injection
    public RickAndMortyCharacterRepositoryAdapter(
            RickAndMortyHttpClient httpClient,
            RickAndMortyUrlParser urlParser) {
        this.httpClient = httpClient;
        this.urlParser = urlParser;
    }

    @Override
    public Character findById(CharacterId id) {
        // ✅ UNA SOLA RESPONSABILIDAD: Orquestación de servicios
        CharacterApiDto characterDto = httpClient.fetchCharacter(id);

        return characterDto.origin()
            .filter(origin -> origin.url() != null && !origin.url().isBlank())
            .map(origin -> {
                LocationId locationId = urlParser.extractLocationId(origin.url());
                LocationApiDto locationDto = httpClient.fetchLocation(locationId);
                return CharacterDomainMapper.toDomain(characterDto, locationDto);
            })
            .orElse(CharacterDomainMapper.toDomain(characterDto));
    }
}
```

## Comparación: Antes vs Después

### ❌ ANTES (Viola SRP):
- **1 clase** con **5 responsabilidades**
- **28 líneas** en un método
- **Inyección de campo**
- **Lógica mezclada** (HTTP + parsing + mapping)

### ✅ DESPUÉS (Cumple SRP):
- **4 clases** especializadas con **1 responsabilidad cada una**
- **Métodos < 15 líneas**
- **Constructor injection**
- **Separación clara** de responsabilidades

## Beneficios de la Refactorización

1. **Testabilidad**: Cada componente se puede mockear independientemente
2. **Mantenibilidad**: Cambios en parsing no afectan HTTP calls
3. **Reutilización**: UrlParser se puede usar en otros adapters
4. **Legibilidad**: Cada clase tiene un propósito claro
5. **SOLID Compliance**: SRP + DIP + OCP cumplidos
6. **Error Handling**: Mejor manejo de excepciones por responsabilidad

## Plan de Implementación

1. ✅ **Crear Value Objects** (`CharacterId`, `LocationId`)
2. ✅ **Crear puertos de dominio** (`CharacterRepository`, `LocationRepository`)
3. ✅ **Extraer UrlParser** como servicio especializado
4. ✅ **Extraer HttpClient** como servicio especializado
5. ✅ **Extraer Mapper** como utility class
6. ✅ **Refactorizar Adapter** para solo orquestar
7. ✅ **Actualizar tests** para nuevas dependencias
8. ✅ **Eliminar código legacy**

# Análisis de Violación SRP en FindCharacterByIdImpl

## Problema Actual: Múltiples Responsabilidades

La clase `FindCharacterByIdImpl` viola SRP al tener estas responsabilidades:

1. **Orquestación**: Coordinar la llamada al ApiClient
2. **Transformación**: Convertir Character → CharacterResponse
3. **Validación implícita**: Asumir que el ID es válido

## Solución: Separación de Responsabilidades

### Paso 1: Crear Value Object para ID
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
```

### Paso 2: Crear Puerto de Dominio Correcto
```java
// domain/ports/in/FindCharacterUseCase.java
package cl.mobdev.rm.domain.ports.in;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.CharacterId;

@FunctionalInterface
public interface FindCharacterUseCase {
    Character execute(CharacterId id);  // ← Solo dominio, no DTOs
}
```

### Paso 3: Implementación de Caso de Uso (Solo Orquestación)
```java
// application/service/FindCharacterService.java
package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.CharacterId;
import cl.mobdev.rm.domain.ports.in.FindCharacterUseCase;
import cl.mobdev.rm.domain.ports.out.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
public class FindCharacterService implements FindCharacterUseCase {

    private final CharacterRepository characterJpaRepository;

    // ✅ Constructor injection (no field injection)
    public FindCharacterService(CharacterRepository characterJpaRepository) {
        this.characterJpaRepository = characterJpaRepository;
    }

    @Override
    public Character execute(CharacterId id) {
        // ✅ UNA SOLA RESPONSABILIDAD: Orquestación
        return characterJpaRepository.findById(id);
    }
}
```

### Paso 4: Separar Transformación en Clase Dedicada
```java
// application/mapper/CharacterResponseMapper.java
package cl.mobdev.rm.application.mapper;

import cl.mobdev.rm.application.dto.CharacterResponse;
import cl.mobdev.rm.domain.model.Character;

public final class CharacterResponseMapper {

    private CharacterResponseMapper() {} // Utility class

    // ✅ UNA SOLA RESPONSABILIDAD: Transformación Domain → DTO
    public static CharacterResponse toResponse(Character character) {
        return new CharacterResponse(
            character.id(),
            character.name(),
            character.status(),
            character.species(),
            character.type(),
            character.episodeCount(),
            character.location().map(loc -> new OriginResponse(
                loc.name(),
                loc.url(),
                loc.dimension(),
                loc.residents().size()
            )).orElse(null)
        );
    }
}
```

### Paso 5: Controller Refactorizado (Responsabilidad de Coordinación Web)
```java
// infrastructure/adapter/in/CharacterController.java
package cl.mobdev.rm.infrastructure.adapter.in;

import cl.mobdev.rm.application.dto.CharacterResponse;
import cl.mobdev.rm.application.mapper.CharacterResponseMapper;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.CharacterId;
import cl.mobdev.rm.domain.ports.in.FindCharacterUseCase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/characters")
@Validated
public class CharacterController {

    private final FindCharacterUseCase findCharacterUseCase;

    // ✅ Constructor injection
    public CharacterController(FindCharacterUseCase findCharacterUseCase) {
        this.findCharacterUseCase = findCharacterUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> findById(
        @PathVariable @Min(1) @Max(999999) Integer id) {

        // ✅ RESPONSABILIDADES CLARAS:
        // 1. Validación (Bean Validation)
        // 2. Conversión a Value Object
        // 3. Llamada a caso de uso
        // 4. Transformación a DTO de vista
        // 5. Envoltorio HTTP

        CharacterId characterId = new CharacterId(id);
        Character character = findCharacterUseCase.execute(characterId);
        CharacterResponse response = CharacterResponseMapper.toResponse(character);

        return ResponseEntity.ok(response);
    }
}
```

## Resumen de Responsabilidades Separadas

| **Clase** | **Responsabilidad Única** | **Cumple SRP** |
|-----------|---------------------------|----------------|
| `CharacterId` | Validación y encapsulación de ID | ✅ |
| `FindCharacterService` | Orquestación de caso de uso | ✅ |
| `CharacterResponseMapper` | Transformación Domain → DTO | ✅ |
| `CharacterController` | Coordinación HTTP/Web | ✅ |

## Beneficios de la Refactorización

1. **Testabilidad**: Cada clase se puede testear independientemente
2. **Mantenibilidad**: Cambios en transformación no afectan orquestación
3. **Reutilización**: Mapper se puede usar en otros endpoints
4. **Principios SOLID**: SRP cumplido, DIP mejorado con constructor injection
5. **Arquitectura Hexagonal**: Dominio puro, puertos correctos

## Violación Original vs. Solución

**ANTES (Viola SRP):**
```java
// ❌ Múltiples responsabilidades en una clase
public CharacterResponse execute(String id) {
     Character character = client.getChararcter(id);      // Orquestación
     return CharacterMapper.mapperToCharacterResponse(character); // Transformación
}
```

**DESPUÉS (Cumple SRP):**
```java
// ✅ Cada clase tiene una responsabilidad única
public Character execute(CharacterId id) {
    return characterJpaRepository.findById(id);  // Solo orquestación
}

public static CharacterResponse toResponse(Character character) {
    // Solo transformación
}
```

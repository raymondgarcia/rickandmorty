package cl.mobdev.rm.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import cl.mobdev.rm.domain.ports.FindCharacterUseCase;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Character Slice Integration Tests")
class CharacterSliceIntegrationTest {

  @Autowired private FindCharacterUseCase findCharacterUseCase;

  @TestConfiguration
  static class TestConfig {
    @Bean
    @Primary
    ExternalCharacterRepository testCharacterRepository() {
      return new InMemoryExternalCharacterRepository();
    }
  }

  @Nested
  @DisplayName("Given valid character data")
  class GivenValidCharacterData {

    @Test
    @DisplayName("When finding character by ID, then returns complete character information")
    void whenFindingCharacterById_thenReturnsCompleteCharacterInfo() {
      // Given
      String characterId = "1";

      // When
      Character result = findCharacterUseCase.execute(characterId);

      // Then
      assertThat(result).isNotNull();
      assertThat(result.id()).isEqualTo(1);
      assertThat(result.name()).isEqualTo("Rick Sanchez");
      assertThat(result.status()).isEqualTo("Alive");
      assertThat(result.species()).isEqualTo("Human");
      assertThat(result.type()).isEqualTo("Scientist");
      assertThat(result.episodeCount()).isEqualTo(51);
      assertThat(result.location()).isPresent();
      assertThat(result.location().get().name()).isEqualTo("Earth (C-137)");
    }

    @Test
    @DisplayName(
        "When finding character without location, then returns character with empty location")
    void whenFindingCharacterWithoutLocation_thenReturnsCharacterWithEmptyLocation() {
      // Given
      String characterId = "2";

      // When
      Character result = findCharacterUseCase.execute(characterId);

      // Then
      assertThat(result).isNotNull();
      assertThat(result.id()).isEqualTo(2);
      assertThat(result.name()).isEqualTo("Morty Smith");
      assertThat(result.location()).isEmpty();
    }
  }

  @Nested
  @DisplayName("Given invalid character data")
  class GivenInvalidCharacterData {

    @Test
    @DisplayName("When finding non-existent character, then throws exception")
    void whenFindingNonExistentCharacter_thenThrowsException() {
      // Given
      String characterId = "999";

      // When & Then
      assertThatThrownBy(() -> findCharacterUseCase.execute(characterId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Character not found");
    }
  }

  static class InMemoryExternalCharacterRepository implements ExternalCharacterRepository {

    private final Location earthC137 =
        new Location(
            "Earth (C-137)",
            "https://rickandmortyapi.com/api/location/1",
            "Dimension C-137",
            List.of("Rick Sanchez", "Morty Smith"));

    @Override
    public Character findCharacter(String id) {
      return switch (id) {
        case "1" -> new Character(
            1, "Rick Sanchez", "Alive", "Human", "Scientist", 51, Optional.of(earthC137));
        case "2" -> new Character(2, "Morty Smith", "Alive", "Human", "", 51, Optional.empty());
        default -> throw new IllegalArgumentException("Character not found with ID: " + id);
      };
    }
  }
}

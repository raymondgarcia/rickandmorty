package cl.mobdev.rm.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import cl.mobdev.rm.application.service.IsCharactersEarthlingService;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Find if Character is from Earth Service Test")
class IsCharactersEarthlingTest {

  @InjectMocks IsCharactersEarthlingService service;

  @Test
  @DisplayName("Should return false if character has not location name is")
  void shouldReturnFalseIfCharacterHasNotLocationName() {
    Character character =
        new Character(
            1,
            "Rick Sanchez",
            "Alive",
            "Human",
            "",
            41,
            Optional.of(
                new Location(
                    null,
                    "https://rickandmortyapi.com/api/location/1",
                    "Earth",
                    java.util.List.of(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2"))));

    boolean isEarthling = service.execute(character);

    assertThat(isEarthling).isFalse();
  }

  @Test
  @DisplayName("Should return true if character is from Earth")
  void shouldReturnTrueIfCharacterIsFromEarth() {
    Character character = createCharacterFromEarth();

    boolean isEarthling = service.execute(character);

    assertThat(isEarthling).isTrue();
  }

  @Test
  @DisplayName("Should return false if character has no location")
  void shouldReturnFalseIfCharacterHasNoLocation() {
    Character character = getCharacterNoLocation();

    boolean isEarthling = service.execute(character);

    assertThat(isEarthling).isFalse();
  }

  @Test
  @DisplayName("Should return false if character is not from Earth")
  public void shouldReturnFalseIfCharacterIsNotFromEarth() {
    Character character = getAlienCharacter();

    boolean isEarthling = service.execute(character);

    assertThat(isEarthling).isFalse();
  }

  private static Character getCharacterNoLocation() {
    return new Character(3, "Summer Smith", "Alive", "Human", "", 16, Optional.empty());
  }

  private Character getAlienCharacter() {
    return new Character(
        2,
        "Morty Smith",
        "Alive",
        "Human",
        "",
        14,
        Optional.of(
            new Location(
                "Dimension C-137",
                "https://rickandmortyapi.com/api/location/2",
                "Dimension C-137",
                java.util.List.of(
                    "https://rickandmortyapi.com/api/character/1",
                    "https://rickandmortyapi.com/api/character/3"))));
  }

  private Character createCharacterFromEarth() {
    return new Character(
        1,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        41,
        Optional.of(
            new Location(
                "Earth (C-137)",
                "https://rickandmortyapi.com/api/location/1",
                "Earth",
                java.util.List.of(
                    "https://rickandmortyapi.com/api/character/1",
                    "https://rickandmortyapi.com/api/character/2"))));
  }
}

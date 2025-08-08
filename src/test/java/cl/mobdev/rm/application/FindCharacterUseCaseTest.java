package cl.mobdev.rm.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.mobdev.rm.application.service.FindCharacterService;
import cl.mobdev.rm.domain.exception.RickAndMortyApiException;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("Find Character Use Case Tests")
class FindCharacterUseCaseTest {

  @Mock ExternalCharacterRepository client;

  @InjectMocks FindCharacterService service;

  private Character createValidCharacterWithLocation() {
    Location location =
        new Location(
            "Earth (C-137)",
            "https://rickandmortyapi.com/api/location/1",
            "Dimension C-137",
            List.of(
                "https://rickandmortyapi.com/api/character/38",
                "https://rickandmortyapi.com/api/character/45",
                "https://rickandmortyapi.com/api/character/71",
                "https://rickandmortyapi.com/api/character/82",
                "https://rickandmortyapi.com/api/character/83",
                "https://rickandmortyapi.com/api/character/92",
                "https://rickandmortyapi.com/api/character/112",
                "https://rickandmortyapi.com/api/character/114",
                "https://rickandmortyapi.com/api/character/116",
                "https://rickandmortyapi.com/api/character/117",
                "https://rickandmortyapi.com/api/character/120",
                "https://rickandmortyapi.com/api/character/127",
                "https://rickandmortyapi.com/api/character/155",
                "https://rickandmortyapi.com/api/character/169",
                "https://rickandmortyapi.com/api/character/175",
                "https://rickandmortyapi.com/api/character/179",
                "https://rickandmortyapi.com/api/character/186",
                "https://rickandmortyapi.com/api/character/201",
                "https://rickandmortyapi.com/api/character/216",
                "https://rickandmortyapi.com/api/character/239",
                "https://rickandmortyapi.com/api/character/271",
                "https://rickandmortyapi.com/api/character/302",
                "https://rickandmortyapi.com/api/character/303",
                "https://rickandmortyapi.com/api/character/338",
                "https://rickandmortyapi.com/api/character/343",
                "https://rickandmortyapi.com/api/character/356",
                "https://rickandmortyapi.com/api/character/394"));

    return new Character(1, "Rick Sanchez", "Alive", "Human", "", 51, Optional.of(location));
  }

  private Character createValidCharacterWithoutLocation() {
    return new Character(1, "Rick Sanchez", "Alive", "Human", "", 51, Optional.empty());
  }

  @Test
  @DisplayName("Should return character without location when location is not available")
  void shouldReturnCharacterWithoutLocation() {
    String characterId = "1";
    Character expectedCharacter = createValidCharacterWithoutLocation();
    when(client.findCharacter(characterId)).thenReturn(expectedCharacter);

    Character actualCharacter = service.execute(characterId);

    assertThat(actualCharacter).isNotNull();
    assertThat(actualCharacter.id()).isEqualTo(1);
    assertThat(actualCharacter.name()).isEqualTo("Rick Sanchez");
    assertThat(actualCharacter.status()).isEqualTo("Alive");
    assertThat(actualCharacter.species()).isEqualTo("Human");
    assertThat(actualCharacter.type()).isEmpty();
    assertThat(actualCharacter.episodeCount()).isEqualTo(51);
    assertThat(actualCharacter.location()).isEmpty();

    verify(client).findCharacter(characterId);
  }

  @Test
  @DisplayName("Should return character with complete location information")
  void shouldReturnCharacterWithLocation() {
    String characterId = "1";
    Character expectedCharacter = createValidCharacterWithLocation();
    when(client.findCharacter(characterId)).thenReturn(expectedCharacter);

    Character actualCharacter = service.execute(characterId);

    assertThat(actualCharacter).isNotNull();
    assertThat(actualCharacter.id()).isEqualTo(1);
    assertThat(actualCharacter.name()).isEqualTo("Rick Sanchez");
    assertThat(actualCharacter.status()).isEqualTo("Alive");
    assertThat(actualCharacter.species()).isEqualTo("Human");
    assertThat(actualCharacter.type()).isEmpty();
    assertThat(actualCharacter.episodeCount()).isEqualTo(51);

    assertThat(actualCharacter.location())
        .isPresent()
        .hasValueSatisfying(
            location -> {
              assertThat(location.name()).isEqualTo("Earth (C-137)");
              assertThat(location.url()).isEqualTo("https://rickandmortyapi.com/api/location/1");
              assertThat(location.dimension()).isEqualTo("Dimension C-137");
              assertThat(location.residents()).asList().hasSize(27);
            });

    verify(client).findCharacter(characterId);
  }

  @Test
  @DisplayName("Should propagate exception when external repository fails")
  void shouldPropagateExceptionWhenRepositoryFails() {
    String characterId = "999";
    RickAndMortyApiException expectedException =
        new RickAndMortyApiException(HttpStatusCode.valueOf(404), "Character not found");
    when(client.findCharacter(characterId)).thenThrow(expectedException);

    assertThatThrownBy(() -> service.execute(characterId))
        .isInstanceOf(RickAndMortyApiException.class)
        .hasMessage("Error 404 NOT_FOUND body Character not found");

    verify(client).findCharacter(characterId);
  }
}

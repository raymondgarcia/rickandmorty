package cl.mobdev.rm.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import cl.mobdev.rm.application.service.FindCharacterService;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.domain.ports.CharacterRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Find Character Service Test")
public class FindCharacterUseCaseTest {

  @Mock CharacterRepository client;

  @InjectMocks FindCharacterService service;

  public Character createValidCharacter() {
    Optional<Location> location =
        Optional.of(
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
                    "https://rickandmortyapi.com/api/character/394")));
    return new Character(1, "Rick Sanchez", "Alive", "Human", "", 51, location);
  }

  public Character createValidCharacterNoLocation() {
    Optional<Location> location = Optional.empty();
    return new Character(1, "Rick Sanchez", "Alive", "Human", "", 51, location);
  }

  @Test
  @DisplayName("Should throw a Exception when Id is not a valid Identifier")
  void shouldReturnCharacterByIdNoLocation() {
    when(client.getChararcter("1")).thenReturn(createValidCharacterNoLocation());

    Character response = service.execute("1");

    assertThat(response.id()).isEqualTo(1);
    assertThat(response.name()).isEqualTo("Rick Sanchez");
    assertThat(response.status()).isEqualTo("Alive");
    assertThat(response.species()).isEqualTo("Human");
    assertThat(response.type()).isEqualTo("");
    assertThat(response.episodeCount()).isEqualTo(51);
    assertThat(response.location()).isEmpty();
  }

  @Test
  @DisplayName("Should return a character by its ID")
  void shouldReturnCharacterById() {
    when(client.getChararcter("1")).thenReturn(createValidCharacter());

    Character response = service.execute("1");

    assertThat(response.id()).isEqualTo(1);
    assertThat(response.name()).isEqualTo("Rick Sanchez");
    assertThat(response.status()).isEqualTo("Alive");
    assertThat(response.species()).isEqualTo("Human");
    assertThat(response.type()).isEqualTo("");
    assertThat(response.episodeCount()).isEqualTo(51);

    assertThat(response.location())
        .isPresent()
        .hasValueSatisfying(
            origin -> {
              assertThat(origin.name()).isEqualTo("Earth (C-137)");
              assertThat(origin.url()).isEqualTo("https://rickandmortyapi.com/api/location/1");
              assertThat(origin.dimension()).isEqualTo("Dimension C-137");
              assertThat(origin.residents().size()).isEqualTo(27);
            });
  }
}

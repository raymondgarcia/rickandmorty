package cl.mobdev.rm.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import cl.mobdev.rm.application.service.GetSortedCharactersWithMartianTypeService;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("Get Characters Service Test")
public class GetCharactersTest {

  @Mock ExternalCharacterRepository client;

  @InjectMocks GetSortedCharactersWithMartianTypeService gerCharactersService;

  @Test
  @DisplayName("Should return all characters")
  public void shouldReturnAllCharacters() {

    // Given
    Location earth =
        new Location(
            "Earth (C-137)",
            "https://rickandmortyapi.com/api/location/1",
            "Dimension C-137",
            List.of(
                "https://rickandmortyapi.com/api/character/1",
                "https://rickandmortyapi.com/api/character/2"));
    Location mars = new Location("Mars", "", "Earth (C-137)", List.of());

    when(client.getAllCharacters())
        .thenReturn(
            List.of(
                new Character(1, "Rick Sanchez", "Alive", "Human", "Human", 41, Optional.of(earth)),
                new Character(
                    2, "Morty Smith", "Alive", "Human", "Ela Hola Mundo", 14, Optional.of(mars))));

    List<Character> characters = gerCharactersService.execute();

    assertThat(characters).isNotNull();
    assertThat(characters.size()).isEqualTo(2);
    assertThat(characters.getFirst().type()).isEqualTo("Human");
    assertThat(characters.getLast().type()).isEqualTo("2l1h4l1m5nd4");
  }
}

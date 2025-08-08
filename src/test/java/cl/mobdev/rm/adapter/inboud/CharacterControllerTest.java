package cl.mobdev.rm.adapter.inboud;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.mobdev.rm.application.service.CharacterService;
import cl.mobdev.rm.domain.exception.RickAndMortyApiException;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.infrastructure.adapter.inbound.CharacterController;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ContextConfiguration(classes = CharacterController.class)
@DisplayName("Character Controller Test")
class CharacterControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CharacterService service;

  @Test
  @DisplayName("should return a Character by their ID")
  void shouldReturnCharacterById() throws Exception {
    when(service.findCharacter(any(String.class))).thenReturn(createValidCharacter());

    mockMvc
        .perform(get("/api/v1/character/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Rick Sanchez"))
        .andExpect(jsonPath("$.status").value("Alive"))
        .andExpect(jsonPath("$.species").value("Human"))
        .andExpect(jsonPath("$.type").value(""))
        .andExpect(jsonPath("$.episode_count").value(51))
        .andExpect(jsonPath("$.origin").isMap())
        .andExpect(jsonPath("$.origin.name").value("Earth (C-137)"))
        .andExpect(jsonPath("$.origin.url").value("https://rickandmortyapi.com/api/location/1"))
        .andExpect(jsonPath("$.origin.dimension").value("Dimension C-137"))
        .andExpect(jsonPath("$.origin.residents").isArray())
        .andExpect(jsonPath("$.origin.residents.length()").value(27));
  }

  @Test
  @DisplayName("should return a Exception and mapped it to 404 when Character not found")
  void shouldThrowException() throws Exception {
    when(service.findCharacter("20000"))
        .thenThrow(new RickAndMortyApiException(HttpStatus.NOT_FOUND, ""));

    mockMvc.perform(get("/api/v1/characters/20000")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Should return 400 when ID is a character")
  void shouldThrowExceptionWithCharacter() throws Exception {
    when(service.findCharacter("A")).thenReturn(createValidCharacter());

    mockMvc.perform(get("/api/v1/character/A")).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should return 400 when ID is a negative")
  void shouldThrowExceptionNegative() throws Exception {
    when(service.findCharacter("-1")).thenReturn(createValidCharacter());

    mockMvc.perform(get("/api/v1/character/-1")).andExpect(status().isBadRequest());
  }

  public Character createValidCharacter() {
    Optional<Location> origin =
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
    return new Character(1, "Rick Sanchez", "Alive", "Human", "", 51, origin);
  }
}

package cl.mobdev.rm.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.infrastructure.adapter.outbound.RickAndMortyExternalCharacterRepositoryAdapter;
import cl.mobdev.rm.infrastructure.client.RickAndMortyHttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Rick and Morty API Contract Tests")
class RickAndMortyApiContractTest {

  private WireMockServer wireMockServer;
  private RickAndMortyExternalCharacterRepositoryAdapter adapter;
  private RestClient restClient;

  @BeforeEach
  void setUp() {
    // Setup WireMock server
    wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
    wireMockServer.start();

    // Configure RestClient to use WireMock server
    restClient = RestClient.builder().baseUrl("http://localhost:8089/api/").build();

    // Create adapter with dependencies
    RickAndMortyHttpClient httpClient = new RickAndMortyHttpClient(restClient);
    adapter = new RickAndMortyExternalCharacterRepositoryAdapter(httpClient);
  }

  @AfterEach
  void tearDown() {
    if (wireMockServer != null) {
      wireMockServer.stop();
    }
  }

  @Nested
  @DisplayName("Character API Contract")
  class CharacterApiContract {

    @Test
    @DisplayName("Should retrieve character with location successfully")
    void shouldRetrieveCharacterWithLocationSuccessfully() {
      // Given - Mock character API response
      wireMockServer.stubFor(
          get(urlEqualTo("/api/character/1"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBody(
                          """
                                {
                                  "id": 1,
                                  "name": "Rick Sanchez",
                                  "status": "Alive",
                                  "species": "Human",
                                  "type": "Scientist",
                                  "gender": "Male",
                                  "origin": {
                                    "name": "Earth (C-137)",
                                    "url": "https://rickandmortyapi.com/api/location/1"
                                  },
                                  "location": {
                                    "name": "Citadel of Ricks",
                                    "url": "https://rickandmortyapi.com/api/location/3"
                                  },
                                  "episode": [
                                    "https://rickandmortyapi.com/api/episode/1",
                                    "https://rickandmortyapi.com/api/episode/2"
                                  ]
                                }
                                """)));

      // Mock location API response
      wireMockServer.stubFor(
          get(urlEqualTo("/api/location/1"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBody(
                          """
                                {
                                  "id": 1,
                                  "name": "Earth (C-137)",
                                  "type": "Planet",
                                  "dimension": "Dimension C-137",
                                  "residents": [
                                    "https://rickandmortyapi.com/api/character/1",
                                    "https://rickandmortyapi.com/api/character/2"
                                  ]
                                }
                                """)));

      // When
      Character result = adapter.findCharacter("1");

      // Then
      assertThat(result).isNotNull();
      assertThat(result.id()).isEqualTo(1);
      assertThat(result.name()).isEqualTo("Rick Sanchez");
      assertThat(result.status()).isEqualTo("Alive");
      assertThat(result.species()).isEqualTo("Human");
      assertThat(result.type()).isEqualTo("Scientist");
      assertThat(result.episodeCount()).isEqualTo(2);

      // Verify location information
      assertThat(result.location()).isPresent();
      assertThat(result.location().get().name()).isEqualTo("Earth (C-137)");
      assertThat(result.location().get().dimension()).isEqualTo("Dimension C-137");
      assertThat(result.location().get().url())
          .isEqualTo("https://rickandmortyapi.com/api/location/1");

      // Verify API calls were made
      wireMockServer.verify(getRequestedFor(urlEqualTo("/api/character/1")));
      wireMockServer.verify(getRequestedFor(urlEqualTo("/api/location/1")));
    }

    @Test
    @DisplayName("Should handle API errors gracefully")
    void shouldHandleApiErrorsGracefully() {
      // Given - API returns 404
      wireMockServer.stubFor(
          get(urlEqualTo("/api/character/999"))
              .willReturn(
                  aResponse()
                      .withStatus(404)
                      .withHeader("Content-Type", "application/json")
                      .withBody(
                          """
                                {
                                  "error": "Character not found"
                                }
                                """)));

      // When & Then
      assertThatThrownBy(() -> adapter.findCharacter("999")).hasMessageContaining("404");

      wireMockServer.verify(getRequestedFor(urlEqualTo("/api/character/999")));
    }
  }
}

package cl.mobdev.rm.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;

/*
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@Testcontainers
@ActiveProfiles("e2e")
@DisplayName("Character API End-to-End Tests")*/
class CharacterApiE2ETest {
  /*
  @Autowired private MockMvc mockMvc;

  private static WireMockServer wireMockServer;

  @BeforeAll
  static void beforeAll() {
    wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8090));
    wireMockServer.start();
  }

  @AfterAll
  static void afterAll() {
    if (wireMockServer != null) {
      wireMockServer.stop();
    }
  }

  @BeforeEach
  void setUp() {
    wireMockServer.resetAll();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("rickandmorty.api.base-url", () -> "http://localhost:8090/api/");
  }

  @TestConfiguration
  static class E2ETestConfig {
    @Bean
    @Primary
    RestClient testRestClient() {
      return RestClient.builder().baseUrl("http://localhost:8090/api/").build();
    }
  }

  @Nested
  @DisplayName("Successful character retrieval scenarios")
  class SuccessfulScenarios {

    @Test
    @DisplayName(
        "GET /api/v1/character/{id} - Should return character with complete location information")
    void shouldReturnCharacterWithCompleteLocationInfo() throws Exception {
      // Given - Mock Rick and Morty API responses
      mockRickSanchezCharacterResponse();
      mockEarthC137LocationResponse();

      // When & Then
      mockMvc
          .perform(MockMvcRequestBuilders.get("/api/v1/character/1"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.id", is(1)))
          .andExpect(jsonPath("$.name", is("Rick Sanchez")))
          .andExpect(jsonPath("$.status", is("Alive")))
          .andExpect(jsonPath("$.species", is("Human")))
          .andExpect(jsonPath("$.type", is("Scientist")))
          .andExpect(jsonPath("$.episodeCount", is(3)))
          .andExpect(jsonPath("$.origin").exists())
          .andExpect(jsonPath("$.origin.name", is("Earth (C-137)")))
          .andExpect(jsonPath("$.origin.url", is("https://rickandmortyapi.com/api/location/1")))
          .andExpect(jsonPath("$.origin.dimension", is("Dimension C-137")))
          .andExpect(jsonPath("$.origin.residents", hasSize(2)))
          .andExpect(jsonPath("$.origin.residents[0]", is("Rick Sanchez")))
          .andExpect(jsonPath("$.origin.residents[1]", is("Morty Smith")));

      // Verify external API calls
      wireMockServer.verify(getRequestedFor(urlEqualTo("/api/character/1")));
      wireMockServer.verify(getRequestedFor(urlEqualTo("/api/location/1")));
    }

    @Test
    @DisplayName("GET /api/v1/character/{id} - Should return character without location")
    void shouldReturnCharacterWithoutLocation() throws Exception {
      // Given - Mock character without origin
      mockMortySmithCharacterResponse();

      // When & Then
      mockMvc
          .perform(MockMvcRequestBuilders.get("/api/v1/character/2"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.id", is(2)))
          .andExpect(jsonPath("$.name", is("Morty Smith")))
          .andExpect(jsonPath("$.status", is("Alive")))
          .andExpect(jsonPath("$.species", is("Human")))
          .andExpect(jsonPath("$.type", is("")))
          .andExpect(jsonPath("$.episodeCount", is(2)))
          .andExpect(jsonPath("$.origin").doesNotExist());

      // Verify only character API was called
      wireMockServer.verify(getRequestedFor(urlEqualTo("/api/character/2")));
      wireMockServer.verify(0, getRequestedFor(urlMatching("/api/location/.*")));
    }
  }

  @Nested
  @DisplayName("Error handling scenarios")
  class ErrorHandlingScenarios {

    @Test
    @DisplayName("GET /api/v1/character/{id} - Should return 404 when character not found")
    void shouldReturn404WhenCharacterNotFound() throws Exception {
      // Given - External API returns 404
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
      mockMvc
          .perform(MockMvcRequestBuilders.get("/api/v1/character/999"))
          .andExpect(status().isNotFound())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.error", is("CHARACTER_NOT_FOUND")))
          .andExpect(jsonPath("$.message", containsString("Character with ID 999 not found")))
          .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("GET /api/v1/character/{id} - Should return 400 for invalid character ID")
    void shouldReturn400ForInvalidCharacterId() throws Exception {
      // When & Then
      mockMvc
          .perform(MockMvcRequestBuilders.get("/api/v1/character/invalid"))
          .andExpect(status().isBadRequest())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.error", is("INVALID_CHARACTER_ID")))
          .andExpect(jsonPath("$.message", containsString("Invalid character ID format")))
          .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("GET /api/v1/character/{id} - Should return 400 for negative character ID")
    void shouldReturn400ForNegativeCharacterId() throws Exception {
      // When & Then
      mockMvc
          .perform(MockMvcRequestBuilders.get("/api/v1/character/-1"))
          .andExpect(status().isBadRequest())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.error", is("INVALID_CHARACTER_ID")))
          .andExpect(jsonPath("$.message", containsString("Character ID must be positive")))
          .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("GET /api/v1/character/{id} - Should return 500 when external API is down")
    void shouldReturn500WhenExternalApiIsDown() throws Exception {
      // Given - External API is down
      wireMockServer.stubFor(
          get(urlEqualTo("/api/character/1"))
              .willReturn(
                  aResponse()
                      .withStatus(500)
                      .withHeader("Content-Type", "application/json")
                      .withBody(
                          """
                                {
                                  "error": "Internal server error"
                                }
                                """)));

      // When & Then
      mockMvc
          .perform(MockMvcRequestBuilders.get("/api/v1/character/1"))
          .andExpect(status().isInternalServerError())
          .andExpect(content().contentType("application/json"))
          .andExpect(jsonPath("$.error", is("EXTERNAL_API_ERROR")))
          .andExpect(jsonPath("$.message", containsString("Error calling external API")))
          .andExpect(jsonPath("$.timestamp").exists());
    }
  }

  @Nested
  @DisplayName("Performance and reliability scenarios")
  class PerformanceAndReliabilityScenarios {

    @Test
    @DisplayName("Should handle high concurrent requests efficiently")
    void shouldHandleHighConcurrentRequestsEfficiently() throws Exception {
      // Given
      mockRickSanchezCharacterResponse();
      mockEarthC137LocationResponse();

      // When - Simulate multiple concurrent requests
      long startTime = System.currentTimeMillis();

      for (int i = 0; i < 10; i++) {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/character/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Rick Sanchez")));
      }

      long endTime = System.currentTimeMillis();
      long totalTime = endTime - startTime;

      // Then - Should complete within reasonable time
      assertThat(totalTime).isLessThan(5000); // Less than 5 seconds for 10 requests
    }

    @Test
    @DisplayName("Should handle external API timeout gracefully")
    void shouldHandleExternalApiTimeoutGracefully() throws Exception {
      // Given - External API with timeout
      wireMockServer.stubFor(
          get(urlEqualTo("/api/character/1"))
              .willReturn(aResponse().withStatus(200).withFixedDelay(10000))); // 10 second delay

      // When & Then
      mockMvc
          .perform(MockMvcRequestBuilders.get("/api/v1/character/1"))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.error", is("EXTERNAL_API_TIMEOUT")))
          .andExpect(jsonPath("$.message", containsString("External API request timed out")));
    }
  }

  // Helper methods for mocking external API responses
  private void mockRickSanchezCharacterResponse() {
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
                                "https://rickandmortyapi.com/api/episode/2",
                                "https://rickandmortyapi.com/api/episode/3"
                              ]
                            }
                            """)));
  }

  private void mockEarthC137LocationResponse() {
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
                                "Rick Sanchez",
                                "Morty Smith"
                              ]
                            }
                            """)));
  }

  private void mockMortySmithCharacterResponse() {
    wireMockServer.stubFor(
        get(urlEqualTo("/api/character/2"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                            {
                              "id": 2,
                              "name": "Morty Smith",
                              "status": "Alive",
                              "species": "Human",
                              "type": "",
                              "gender": "Male",
                              "origin": {
                                "name": "unknown",
                                "url": ""
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
  }*/
}

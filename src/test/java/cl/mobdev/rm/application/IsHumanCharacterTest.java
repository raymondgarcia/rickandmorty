package cl.mobdev.rm.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import cl.mobdev.rm.application.service.IsHumanCharacterService;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Is Human Character Test")
public class IsHumanCharacterTest {

  @Mock ExternalCharacterRepository client;

  @InjectMocks IsHumanCharacterService service;

  @Test
  @DisplayName("Should return true if character is human")
  public void shouldReturnTrueIfCharacterIsHuman() {

    when(client.findCharacter("1"))
        .thenReturn(new Character(1, "Rick Sanchez", "Alive", "Human", "", 41, Optional.empty()));

    boolean result = service.execute("1");

    assertThat(result).isTrue();
  }
}

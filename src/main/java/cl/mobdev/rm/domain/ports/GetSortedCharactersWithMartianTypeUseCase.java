package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;
import java.util.List;

@FunctionalInterface
public interface GetSortedCharactersWithMartianTypeUseCase {
  List<Character> execute();
}

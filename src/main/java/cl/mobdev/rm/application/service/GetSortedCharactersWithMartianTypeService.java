package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import cl.mobdev.rm.domain.ports.GetSortedCharactersWithMartianTypeUseCase;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetSortedCharactersWithMartianTypeService
    implements GetSortedCharactersWithMartianTypeUseCase {

  private final ExternalCharacterRepository characterRepository;

  public GetSortedCharactersWithMartianTypeService(
      ExternalCharacterRepository characterRepository) {
    this.characterRepository = characterRepository;
  }

  public List<Character> execute() {
    return characterRepository.getAllCharacters().stream()
        .sorted(Comparator.comparing(Character::name))
        .map(
            character -> {
              var locationOpt = character.location();
              if (locationOpt.isPresent() && "Mars".contains(locationOpt.get().name())) {
                return character.withType(
                    MartianTypeTranslatorService.translateToMartian(character.type()));
              }
              return character;
            })
        .collect(Collectors.toList());
  }
}

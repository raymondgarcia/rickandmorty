package cl.mobdev.rm.infrastructure.adapter.outbound;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.LocalCharacterRepository;
import cl.mobdev.rm.infrastructure.entity.CharacterEntity;
import cl.mobdev.rm.infrastructure.mapper.CharacterDomainMapper;
import org.springframework.stereotype.Component;

@Component
public class CharacterRepositoryAdapter implements LocalCharacterRepository {

  private final CharacterRepository characterRepository;

  public CharacterRepositoryAdapter(CharacterRepository characterRepository) {
    this.characterRepository = characterRepository;
  }

  @Override
  public Character save(Character character) {
    CharacterEntity entity = CharacterDomainMapper.toEntity(character);
    CharacterEntity savedCharacter = characterRepository.save(entity);
    return CharacterDomainMapper.toDomain(savedCharacter);
  }
}

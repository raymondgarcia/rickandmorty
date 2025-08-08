package cl.mobdev.rm.infrastructure.adapter.outbound;

import cl.mobdev.rm.domain.exception.RickAndMortyApiException;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.CharacterRepository;
import cl.mobdev.rm.infrastructure.entity.CharacterEntity;
import cl.mobdev.rm.infrastructure.mapper.CharacterDomainMapper;
import java.util.Optional;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class CharacterRepositoryAdapter implements CharacterRepository {

  private final CharacterJpaRepository characterJpaRepository;

  public CharacterRepositoryAdapter(CharacterJpaRepository characterJpaRepository) {
    this.characterJpaRepository = characterJpaRepository;
  }

  @Override
  public Character save(Character character) {
    if (existByApiCharacterId(character.id())) {
      throw new RickAndMortyApiException(
          HttpStatusCode.valueOf(409), "Character with ID " + character.id() + " already exists.");
    }
    CharacterEntity entity = CharacterDomainMapper.toEntity(character);
    CharacterEntity savedCharacter = characterJpaRepository.save(entity);
    return CharacterDomainMapper.toDomain(savedCharacter);
  }

  @Override
  public Optional<Character> findByApiCharacterId(Integer id) {
    return characterJpaRepository.findByApiCharacterId(id).map(CharacterDomainMapper::toDomain);
  }

  @Override
  public boolean existByApiCharacterId(Integer id) {
    return characterJpaRepository.existsByApiCharacterId(id);
  }
}

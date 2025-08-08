package cl.mobdev.rm.infrastructure.adapter.outbound;

import cl.mobdev.rm.infrastructure.entity.CharacterEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterJpaRepository extends JpaRepository<CharacterEntity, Integer> {
  Optional<CharacterEntity> findByApiCharacterId(Integer apiCharacterId);

  boolean existsByApiCharacterId(Integer apiCharacterId);

  Optional<CharacterEntity> findById(Integer id);
}

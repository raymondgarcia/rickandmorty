package cl.mobdev.rm.infrastructure.adapter.outbound;

import cl.mobdev.rm.infrastructure.entity.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Integer> {}

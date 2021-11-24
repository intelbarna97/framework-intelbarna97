package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.CharactersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharactersRepository extends JpaRepository<CharactersEntity, Integer> {
}

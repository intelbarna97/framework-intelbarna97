package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.ChaptersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChaptersRepository extends JpaRepository<ChaptersEntity, Integer> {
}

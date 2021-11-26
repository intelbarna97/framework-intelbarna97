package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.ParagraphsEntity;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParagraphsRepository extends JpaRepository<ParagraphsEntity, Integer> {
}

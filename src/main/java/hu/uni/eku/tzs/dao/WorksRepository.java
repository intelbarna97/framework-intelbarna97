package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.WorksEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorksRepository extends JpaRepository<WorksEntity, Integer> {
}

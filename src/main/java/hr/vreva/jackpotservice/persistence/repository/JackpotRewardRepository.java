package hr.vreva.jackpotservice.persistence.repository;

import hr.vreva.jackpotservice.persistence.entity.JackpotRewardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JackpotRewardRepository extends JpaRepository<JackpotRewardEntity, Long> {
    Optional<JackpotRewardEntity> findByBetId(String betId);
}

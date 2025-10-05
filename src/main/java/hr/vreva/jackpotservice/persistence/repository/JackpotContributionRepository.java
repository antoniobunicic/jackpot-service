package hr.vreva.jackpotservice.persistence.repository;

import hr.vreva.jackpotservice.persistence.entity.JackpotContributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JackpotContributionRepository extends JpaRepository<JackpotContributionEntity, Long> {
    Optional<JackpotContributionEntity> findByBetId(String betId);
}

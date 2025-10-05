package hr.vreva.jackpotservice.persistence.repository;

import hr.vreva.jackpotservice.persistence.entity.JackpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface JackpotRepository extends JpaRepository<JackpotEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT j FROM JackpotEntity j WHERE j.id = :id")
    Optional<JackpotEntity> findByIdWithLock(Long id);
}

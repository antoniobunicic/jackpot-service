package hr.vreva.jackpotservice.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jackpot_contribution")
public class JackpotContributionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String betId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long jackpotId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal stakeAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal contributionAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentJackpotAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public JackpotContributionEntity() {
    }

    public JackpotContributionEntity(Long id, String betId, String userId, Long jackpotId,
                                     BigDecimal stakeAmount, BigDecimal contributionAmount,
                                     BigDecimal currentJackpotAmount, LocalDateTime createdAt) {
        this.id = id;
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.stakeAmount = stakeAmount;
        this.contributionAmount = contributionAmount;
        this.currentJackpotAmount = currentJackpotAmount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getJackpotId() {
        return jackpotId;
    }

    public void setJackpotId(Long jackpotId) {
        this.jackpotId = jackpotId;
    }

    public BigDecimal getStakeAmount() {
        return stakeAmount;
    }

    public void setStakeAmount(BigDecimal stakeAmount) {
        this.stakeAmount = stakeAmount;
    }

    public BigDecimal getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(BigDecimal contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    public BigDecimal getCurrentJackpotAmount() {
        return currentJackpotAmount;
    }

    public void setCurrentJackpotAmount(BigDecimal currentJackpotAmount) {
        this.currentJackpotAmount = currentJackpotAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String betId;
        private String userId;
        private Long jackpotId;
        private BigDecimal stakeAmount;
        private BigDecimal contributionAmount;
        private BigDecimal currentJackpotAmount;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder betId(String betId) {
            this.betId = betId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder jackpotId(Long jackpotId) {
            this.jackpotId = jackpotId;
            return this;
        }

        public Builder stakeAmount(BigDecimal stakeAmount) {
            this.stakeAmount = stakeAmount;
            return this;
        }

        public Builder contributionAmount(BigDecimal contributionAmount) {
            this.contributionAmount = contributionAmount;
            return this;
        }

        public Builder currentJackpotAmount(BigDecimal currentJackpotAmount) {
            this.currentJackpotAmount = currentJackpotAmount;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public JackpotContributionEntity build() {
            return new JackpotContributionEntity(id, betId, userId, jackpotId, stakeAmount,
                    contributionAmount, currentJackpotAmount, createdAt);
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

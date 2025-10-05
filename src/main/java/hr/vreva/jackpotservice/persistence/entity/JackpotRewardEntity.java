package hr.vreva.jackpotservice.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jackpot_reward")
public class JackpotRewardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String betId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long jackpotId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal jackpotRewardAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public JackpotRewardEntity() {
    }

    public JackpotRewardEntity(Long id, String betId, String userId, Long jackpotId,
                               BigDecimal jackpotRewardAmount, LocalDateTime createdAt) {
        this.id = id;
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.jackpotRewardAmount = jackpotRewardAmount;
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

    public BigDecimal getJackpotRewardAmount() {
        return jackpotRewardAmount;
    }

    public void setJackpotRewardAmount(BigDecimal jackpotRewardAmount) {
        this.jackpotRewardAmount = jackpotRewardAmount;
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
        private BigDecimal jackpotRewardAmount;
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

        public Builder jackpotRewardAmount(BigDecimal jackpotRewardAmount) {
            this.jackpotRewardAmount = jackpotRewardAmount;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public JackpotRewardEntity build() {
            return new JackpotRewardEntity(id, betId, userId, jackpotId, jackpotRewardAmount, createdAt);
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

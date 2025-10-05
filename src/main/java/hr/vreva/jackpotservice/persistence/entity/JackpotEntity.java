package hr.vreva.jackpotservice.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jackpot")
public class JackpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal initialPoolValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentPoolValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType contributionType;

    @Column
    private Double contributionPercentage;

    @Column
    private Double contributionDecayRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType;

    @Column
    private Double rewardPercentage;

    @Column(precision = 19, scale = 2)
    private BigDecimal rewardPoolLimit;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    // Default constructor
    public JackpotEntity() {
    }

    public JackpotEntity(Long id, String name, BigDecimal initialPoolValue, BigDecimal currentPoolValue,
                         ContributionType contributionType, Double contributionPercentage,
                         Double contributionDecayRate, RewardType rewardType, Double rewardPercentage,
                         BigDecimal rewardPoolLimit, LocalDateTime createdAt, LocalDateTime updatedAt, Long version) {
        this.id = id;
        this.name = name;
        this.initialPoolValue = initialPoolValue;
        this.currentPoolValue = currentPoolValue;
        this.contributionType = contributionType;
        this.contributionPercentage = contributionPercentage;
        this.contributionDecayRate = contributionDecayRate;
        this.rewardType = rewardType;
        this.rewardPercentage = rewardPercentage;
        this.rewardPoolLimit = rewardPoolLimit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInitialPoolValue() {
        return initialPoolValue;
    }

    public void setInitialPoolValue(BigDecimal initialPoolValue) {
        this.initialPoolValue = initialPoolValue;
    }

    public BigDecimal getCurrentPoolValue() {
        return currentPoolValue;
    }

    public void setCurrentPoolValue(BigDecimal currentPoolValue) {
        this.currentPoolValue = currentPoolValue;
    }

    public ContributionType getContributionType() {
        return contributionType;
    }

    public void setContributionType(ContributionType contributionType) {
        this.contributionType = contributionType;
    }

    public Double getContributionPercentage() {
        return contributionPercentage;
    }

    public void setContributionPercentage(Double contributionPercentage) {
        this.contributionPercentage = contributionPercentage;
    }

    public Double getContributionDecayRate() {
        return contributionDecayRate;
    }

    public void setContributionDecayRate(Double contributionDecayRate) {
        this.contributionDecayRate = contributionDecayRate;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Double getRewardPercentage() {
        return rewardPercentage;
    }

    public void setRewardPercentage(Double rewardPercentage) {
        this.rewardPercentage = rewardPercentage;
    }

    public BigDecimal getRewardPoolLimit() {
        return rewardPoolLimit;
    }

    public void setRewardPoolLimit(BigDecimal rewardPoolLimit) {
        this.rewardPoolLimit = rewardPoolLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private BigDecimal initialPoolValue;
        private BigDecimal currentPoolValue;
        private ContributionType contributionType;
        private Double contributionPercentage;
        private Double contributionDecayRate;
        private RewardType rewardType;
        private Double rewardPercentage;
        private BigDecimal rewardPoolLimit;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder initialPoolValue(BigDecimal initialPoolValue) {
            this.initialPoolValue = initialPoolValue;
            return this;
        }

        public Builder currentPoolValue(BigDecimal currentPoolValue) {
            this.currentPoolValue = currentPoolValue;
            return this;
        }

        public Builder contributionType(ContributionType contributionType) {
            this.contributionType = contributionType;
            return this;
        }

        public Builder contributionPercentage(Double contributionPercentage) {
            this.contributionPercentage = contributionPercentage;
            return this;
        }

        public Builder contributionDecayRate(Double contributionDecayRate) {
            this.contributionDecayRate = contributionDecayRate;
            return this;
        }

        public Builder rewardType(RewardType rewardType) {
            this.rewardType = rewardType;
            return this;
        }

        public Builder rewardPercentage(Double rewardPercentage) {
            this.rewardPercentage = rewardPercentage;
            return this;
        }

        public Builder rewardPoolLimit(BigDecimal rewardPoolLimit) {
            this.rewardPoolLimit = rewardPoolLimit;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public JackpotEntity build() {
            return new JackpotEntity(id, name, initialPoolValue, currentPoolValue, contributionType,
                    contributionPercentage, contributionDecayRate, rewardType, rewardPercentage,
                    rewardPoolLimit, createdAt, updatedAt, version);
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ContributionType {
        FIXED,
        VARIABLE
    }

    public enum RewardType {
        FIXED,
        VARIABLE
    }
}

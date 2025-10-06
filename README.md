# Jackpot Service

A Spring Boot backend application for managing jackpot bet contributions and rewards with Kafka integration.

## Overview

This application processes bets through Kafka, manages jackpot pool contributions using different strategies, and evaluates jackpot rewards with configurable probability mechanisms.

## Features

- **Bet Submission API**: Publish bets to Kafka topic `jackpot-bets`
- **Kafka Consumer**: Asynchronously process bets and contribute to jackpot pools
- **Strategy Pattern**: Support for multiple contribution and reward calculation strategies
  - **Fixed Contribution**: Constant percentage of bet amount
  - **Variable Contribution**: Percentage decreases as jackpot pool grows
  - **Fixed Reward**: Constant win probability
  - **Variable Reward**: Win probability increases as pool grows (100% at limit)
- **Reward Evaluation API**: Check if a bet wins the jackpot
- **Thread-Safe Pool Updates**: Pessimistic locking for concurrent operations
- **In-Memory Database**: H2 database for development and testing

## Setup and Running

### 1. Start Kafka

You need a running Kafka instance. Using Docker:

```bash
docker run -d --name kafka -p 9092:9092 \
  apache/kafka:latest
```

Or use Docker Compose (added to `compose.yaml`):

```yaml
services:
  kafka:
    image: apache/kafka:latest
    ports:
      - "9092:9092"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_LISTENERS: PLAINTEXT://localhost:9092,CONTROLLER://localhost:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@localhost:9093
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_NUM_PARTITIONS: 3
```

Then run:
```bash
docker compose up -d
```

### 2. Build the Application

```bash
./gradlew build
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Submit a Bet

**POST** `/api/bets`

Publishes a bet to Kafka for async processing.

**Request Body:**
```json
{
  "betId": "bet-12345",
  "userId": "user-001",
  "jackpotId": 1,
  "betAmount": 100.00
}
```

**Response:**
```json
{
  "betId": "bet-12345",
  "status": "PUBLISHED",
  "message": "Bet successfully published to Kafka"
}
```

### 2. Evaluate Jackpot Reward

**POST** `/api/bets/bet-12345/evaluate-reward`

Checks if a bet wins the jackpot and returns the result.

**Response (Winner):**
```json
{
  "betId": "bet-12345",
  "won": true,
  "rewardAmount": 15750.00,
  "message": "Congratulations! You won the jackpot!"
}
```

**Response (No Win):**
```json
{
  "betId": "bet-12345",
  "won": false,
  "rewardAmount": 0,
  "message": "Better luck next time!"
}
```

## Sample Jackpots

The application initializes with three sample jackpots:

### Jackpot ID 1: Fixed Classic Jackpot
- **Type**: Fixed Contribution / Fixed Reward
- **Initial Pool**: €10,000
- **Contribution**: 5% of bet amount
- **Win Chance**: 1% (constant)

### Jackpot ID 2: Progressive Mega Jackpot
- **Type**: Variable Contribution / Variable Reward
- **Initial Pool**: €50,000
- **Contribution**: Starts at 10%, decreases as pool grows
- **Win Chance**: Starts at 0.1%, increases to 100% at €100,000

### Jackpot ID 3: Daily Jackpot
- **Type**: Fixed Contribution / Variable Reward
- **Initial Pool**: €5,000
- **Contribution**: 2.5% of bet amount
- **Win Chance**: Starts at 0.5%, increases to 100% at €20,000

## Database Console

Access the H2 database console at:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:jackpotdb`
- **Username**: `sa`
- **Password**: (empty)
-- Insert initial jackpot data

-- Fixed Contribution / Fixed Reward jackpot
INSERT INTO jackpot (
    name,
    initial_pool_value,
    current_pool_value,
    contribution_type,
    contribution_percentage,
    contribution_decay_rate,
    reward_type,
    reward_percentage,
    reward_pool_limit,
    version,
    created_at,
    updated_at
) VALUES (
    'Fixed Classic Jackpot',
    10000.00,
    10000.00,
    'FIXED',
    5.0,
    NULL,
    'FIXED',
    1.0,
    NULL,
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Variable Contribution / Variable Reward jackpot
INSERT INTO jackpot (
    name,
    initial_pool_value,
    current_pool_value,
    contribution_type,
    contribution_percentage,
    contribution_decay_rate,
    reward_type,
    reward_percentage,
    reward_pool_limit,
    version,
    created_at,
    updated_at
) VALUES (
    'Progressive Mega Jackpot',
    50000.00,
    50000.00,
    'VARIABLE',
    10.0,
    5.0,
    'VARIABLE',
    0.10,
    100000.00,
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Fixed Contribution / Variable Reward jackpot
INSERT INTO jackpot (
    name,
    initial_pool_value,
    current_pool_value,
    contribution_type,
    contribution_percentage,
    contribution_decay_rate,
    reward_type,
    reward_percentage,
    reward_pool_limit,
    version,
    created_at,
    updated_at
) VALUES (
    'Daily Jackpot',
    5000.00,
    5000.00,
    'FIXED',
    2.5,
    NULL,
    'VARIABLE',
    0.50,
    20000.00,
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

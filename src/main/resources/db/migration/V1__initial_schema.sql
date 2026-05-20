CREATE TABLE users (

    id UUID PRIMARY KEY,

    email VARCHAR(255) NOT NULL UNIQUE,

    full_name VARCHAR(255) NOT NULL,

    cohort VARCHAR(100),

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);



CREATE TABLE membership_plans (

    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    plan_type VARCHAR(50) NOT NULL,

    price NUMERIC(10,2) NOT NULL,

    validity_days INTEGER NOT NULL,

    active BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);



CREATE TABLE membership_tiers (

    id UUID PRIMARY KEY,

    tier_type VARCHAR(50) NOT NULL UNIQUE,

    display_name VARCHAR(100) NOT NULL,

    priority INTEGER NOT NULL,

    active BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);



CREATE TABLE benefits (

    id UUID PRIMARY KEY,

    benefit_type VARCHAR(100) NOT NULL UNIQUE,

    name VARCHAR(255) NOT NULL,

    configuration JSONB NOT NULL,

    active BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);



CREATE TABLE tier_benefits (

    id UUID PRIMARY KEY,

    tier_id UUID NOT NULL REFERENCES membership_tiers(id),

    benefit_id UUID NOT NULL REFERENCES benefits(id),

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uq_tier_benefit
       UNIQUE(tier_id, benefit_id)
);



CREATE TABLE tier_rules (

    id UUID PRIMARY KEY,

    tier_id UUID NOT NULL REFERENCES membership_tiers(id),

    rule_type VARCHAR(100) NOT NULL,

    operator_type VARCHAR(50) NOT NULL,

    threshold_value VARCHAR(255) NOT NULL,

    active BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);



CREATE TABLE subscriptions (

    id UUID PRIMARY KEY,

    user_id UUID NOT NULL REFERENCES users(id),

    membership_plan_id UUID NOT NULL REFERENCES membership_plans(id),

    current_tier_id UUID NOT NULL REFERENCES membership_tiers(id),

    status VARCHAR(50) NOT NULL,

    start_date TIMESTAMP NOT NULL,

    expiry_date TIMESTAMP NOT NULL,

    version BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);



CREATE TABLE subscription_history (

    id UUID PRIMARY KEY,

    subscription_id UUID NOT NULL REFERENCES subscriptions(id),

    action_type VARCHAR(100) NOT NULL,

    previous_tier VARCHAR(50),

    new_tier VARCHAR(50),

    reason VARCHAR(500),

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);



CREATE UNIQUE INDEX uq_active_subscription_per_user ON subscriptions(user_id) WHERE status = 'ACTIVE';
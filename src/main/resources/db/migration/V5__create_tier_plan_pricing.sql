CREATE TABLE tier_plan_pricing
(
    id                 UUID PRIMARY KEY,

    membership_tier_id UUID           NOT NULL,
    membership_plan_id UUID           NOT NULL,

    price              NUMERIC(10, 2) NOT NULL,
    currency           VARCHAR(10)    NOT NULL DEFAULT 'INR',

    active             BOOLEAN        NOT NULL DEFAULT TRUE,
    version            BIGINT         NOT NULL DEFAULT 0,
    created_at         TIMESTAMP      NOT NULL,
    updated_at         TIMESTAMP      NOT NULL,

    CONSTRAINT fk_tpp_tier
        FOREIGN KEY (membership_tier_id)
            REFERENCES membership_tiers (id),

    CONSTRAINT fk_tpp_plan
        FOREIGN KEY (membership_plan_id)
            REFERENCES membership_plans (id),

    CONSTRAINT uk_tier_plan
        UNIQUE (membership_tier_id, membership_plan_id)
);

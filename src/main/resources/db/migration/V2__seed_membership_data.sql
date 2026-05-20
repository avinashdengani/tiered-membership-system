INSERT INTO membership_plans (
    id,
    name,
    plan_type,
    price,
    validity_days,
    active,
    created_at,
    updated_at
)
VALUES
    (
        gen_random_uuid(),
        'Monthly Membership',
        'MONTHLY',
        499.00,
        30,
        true,
        now(),
        now()
    ),
    (
        gen_random_uuid(),
        'Quarterly Membership',
        'QUARTERLY',
        1299.00,
        90,
        true,
        now(),
        now()
    ),
    (
        gen_random_uuid(),
        'Yearly Membership',
        'YEARLY',
        4499.00,
        365,
        true,
        now(),
        now()
    );



INSERT INTO membership_tiers (
    id,
    tier_type,
    display_name,
    priority,
    active,
    created_at,
    updated_at
)
VALUES
    (
        gen_random_uuid(),
        'SILVER',
        'Silver',
        1,
        true,
        now(),
        now()
    ),
    (
        gen_random_uuid(),
        'GOLD',
        'Gold',
        2,
        true,
        now(),
        now()
    ),
    (
        gen_random_uuid(),
        'PLATINUM',
        'Platinum',
        3,
        true,
        now(),
        now()
    );



INSERT INTO benefits (
    id,
    benefit_type,
    name,
    configuration,
    active,
    created_at,
    updated_at
)
VALUES
    (
        gen_random_uuid(),
        'FREE_DELIVERY',
        'Free Delivery',
        '{"enabled": true}',
        true,
        now(),
        now()
    ),
    (
        gen_random_uuid(),
        'EARLY_ACCESS',
        'Early Access Sale',
        '{"enabled": true}',
        true,
        now(),
        now()
    ),
    (
        gen_random_uuid(),
        'PRIORITY_SUPPORT',
        'Priority Support',
        '{"slaHours": 2}',
        true,
        now(),
        now()
    );
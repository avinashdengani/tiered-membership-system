INSERT INTO tier_benefits (
    id,
    tier_id,
    benefit_id,
    created_at,
    updated_at
)
SELECT
    gen_random_uuid(),
    membership_tiers.id,
    benefits.id,
    now(),
    now()
FROM (
    VALUES
        ('SILVER', 'FREE_DELIVERY'),
        ('GOLD', 'FREE_DELIVERY'),
        ('GOLD', 'EARLY_ACCESS'),
        ('PLATINUM', 'FREE_DELIVERY'),
        ('PLATINUM', 'EARLY_ACCESS'),
        ('PLATINUM', 'PRIORITY_SUPPORT')
) AS tier_benefit_seed(tier_type, benefit_type)
JOIN membership_tiers
    ON membership_tiers.tier_type = tier_benefit_seed.tier_type
JOIN benefits
    ON benefits.benefit_type = tier_benefit_seed.benefit_type
ON CONFLICT (tier_id, benefit_id) DO NOTHING;

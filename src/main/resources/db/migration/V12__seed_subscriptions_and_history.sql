-- ============================================================
-- Seed Subscriptions and Subscription History
--
-- Subscriptions reflect the tier state AFTER orders are in.
-- History tells the full lifecycle story for each user.
--
-- Active subscriptions: john, emma, michael, olivia, william, sophia
-- Cancelled subscription: james
-- ============================================================


-- ============================================================
-- SUBSCRIPTIONS
-- ============================================================

-- john.smith → PLATINUM + YEARLY (30 orders, ₹28,500, PREMIUM_USERS cohort)
INSERT INTO subscriptions (
    id, user_id, membership_plan_id, current_tier_id,
    tier_plan_pricing_id, amount_paid,
    status, start_date, expiry_date, created_at, updated_at
)
SELECT
    gen_random_uuid(), u.id, mp.id, mt.id, tpp.id, tpp.price,
    'ACTIVE',
    NOW() - INTERVAL '90 days',
    NOW() + INTERVAL '275 days',
    NOW(), NOW()
FROM users u
    JOIN membership_plans   mp  ON mp.plan_type            = 'YEARLY'
    JOIN membership_tiers   mt  ON mt.tier_type             = 'PLATINUM'
    JOIN tier_plan_pricing  tpp ON tpp.membership_tier_id   = mt.id
    AND tpp.membership_plan_id   = mp.id
WHERE u.email = 'john.smith@test.com';


-- emma.johnson → GOLD + QUARTERLY (12 orders, ₹7,750, PREMIUM_USERS but spend < 15000)
INSERT INTO subscriptions (
    id, user_id, membership_plan_id, current_tier_id,
    tier_plan_pricing_id, amount_paid,
    status, start_date, expiry_date, created_at, updated_at
)
SELECT
    gen_random_uuid(), u.id, mp.id, mt.id, tpp.id, tpp.price,
    'ACTIVE',
    NOW() - INTERVAL '30 days',
    NOW() + INTERVAL '60 days',
    NOW(), NOW()
FROM users u
    JOIN membership_plans   mp  ON mp.plan_type            = 'QUARTERLY'
    JOIN membership_tiers   mt  ON mt.tier_type             = 'GOLD'
    JOIN tier_plan_pricing  tpp ON tpp.membership_tier_id   = mt.id
    AND tpp.membership_plan_id   = mp.id
WHERE u.email = 'emma.johnson@test.com';


-- michael.brown → GOLD + MONTHLY (15 orders, ₹9,900, REGULAR cohort blocks Platinum)
INSERT INTO subscriptions (
    id, user_id, membership_plan_id, current_tier_id,
    tier_plan_pricing_id, amount_paid,
    status, start_date, expiry_date, created_at, updated_at
)
SELECT
    gen_random_uuid(), u.id, mp.id, mt.id, tpp.id, tpp.price,
    'ACTIVE',
    NOW() - INTERVAL '10 days',
    NOW() + INTERVAL '20 days',
    NOW(), NOW()
FROM users u
    JOIN membership_plans   mp  ON mp.plan_type            = 'MONTHLY'
    JOIN membership_tiers   mt  ON mt.tier_type             = 'GOLD'
    JOIN tier_plan_pricing  tpp ON tpp.membership_tier_id   = mt.id
    AND tpp.membership_plan_id   = mp.id
WHERE u.email = 'michael.brown@test.com';


-- olivia.davis → SILVER + MONTHLY (3 orders, ₹1,200 — below both GOLD thresholds)
INSERT INTO subscriptions (
    id, user_id, membership_plan_id, current_tier_id,
    tier_plan_pricing_id, amount_paid,
    status, start_date, expiry_date, created_at, updated_at
)
SELECT
    gen_random_uuid(), u.id, mp.id, mt.id, tpp.id, tpp.price,
    'ACTIVE',
    NOW() - INTERVAL '5 days',
    NOW() + INTERVAL '25 days',
    NOW(), NOW()
FROM users u
    JOIN membership_plans   mp  ON mp.plan_type            = 'MONTHLY'
    JOIN membership_tiers   mt  ON mt.tier_type             = 'SILVER'
    JOIN tier_plan_pricing  tpp ON tpp.membership_tier_id   = mt.id
    AND tpp.membership_plan_id   = mp.id
WHERE u.email = 'olivia.davis@test.com';


-- william.miller → SILVER + MONTHLY (0 orders — live evaluate-tier demo user)
INSERT INTO subscriptions (
    id, user_id, membership_plan_id, current_tier_id,
    tier_plan_pricing_id, amount_paid,
    status, start_date, expiry_date, created_at, updated_at
)
SELECT
    gen_random_uuid(), u.id, mp.id, mt.id, tpp.id, tpp.price,
    'ACTIVE',
    NOW() - INTERVAL '2 days',
    NOW() + INTERVAL '28 days',
    NOW(), NOW()
FROM users u
    JOIN membership_plans   mp  ON mp.plan_type            = 'MONTHLY'
    JOIN membership_tiers   mt  ON mt.tier_type             = 'SILVER'
    JOIN tier_plan_pricing  tpp ON tpp.membership_tier_id   = mt.id
    AND tpp.membership_plan_id   = mp.id
WHERE u.email = 'william.miller@test.com';


-- sophia.wilson → GOLD + YEARLY (12 orders, ₹7,300 — no cohort, earned GOLD via rules)
INSERT INTO subscriptions (
    id, user_id, membership_plan_id, current_tier_id,
    tier_plan_pricing_id, amount_paid,
    status, start_date, expiry_date, created_at, updated_at
)
SELECT
    gen_random_uuid(), u.id, mp.id, mt.id, tpp.id, tpp.price,
    'ACTIVE',
    NOW() - INTERVAL '45 days',
    NOW() + INTERVAL '320 days',
    NOW(), NOW()
FROM users u
    JOIN membership_plans   mp  ON mp.plan_type            = 'YEARLY'
    JOIN membership_tiers   mt  ON mt.tier_type             = 'GOLD'
    JOIN tier_plan_pricing  tpp ON tpp.membership_tier_id   = mt.id
    AND tpp.membership_plan_id   = mp.id
WHERE u.email = 'sophia.wilson@test.com';


-- james.anderson → SILVER + MONTHLY → CANCELLED
-- CANCELLED status does not trigger the partial unique index,
INSERT INTO subscriptions (
    id, user_id, membership_plan_id, current_tier_id,
    tier_plan_pricing_id, amount_paid,
    status, start_date, expiry_date, created_at, updated_at
)
SELECT
    gen_random_uuid(), u.id, mp.id, mt.id, tpp.id, tpp.price,
    'CANCELLED',
    NOW() - INTERVAL '60 days',
    NOW() - INTERVAL '30 days',
    NOW(), NOW()
FROM users u
    JOIN membership_plans   mp  ON mp.plan_type            = 'MONTHLY'
    JOIN membership_tiers   mt  ON mt.tier_type             = 'SILVER'
    JOIN tier_plan_pricing  tpp ON tpp.membership_tier_id   = mt.id
    AND tpp.membership_plan_id   = mp.id
WHERE u.email = 'james.anderson@test.com';


-- ============================================================
-- SUBSCRIPTION HISTORY
-- Full lifecycle story per user. Shows the audit trail that
-- ============================================================

-- john.smith: SUBSCRIBED (Silver) → UPGRADED (Gold) → UPGRADED (Platinum)
INSERT INTO subscription_history (id, subscription_id, action_type, previous_tier, new_tier, reason, created_at, updated_at)
SELECT gen_random_uuid(), s.id, 'SUBSCRIBED', NULL,     'SILVER',   'Initial tier assignment',        NOW() - INTERVAL '90 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'john.smith@test.com'
UNION ALL
SELECT gen_random_uuid(), s.id, 'UPGRADED',   'SILVER', 'GOLD',     'Tier upgraded after evaluation', NOW() - INTERVAL '60 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'john.smith@test.com'
UNION ALL
SELECT gen_random_uuid(), s.id, 'UPGRADED',   'GOLD',   'PLATINUM', 'Tier upgraded after evaluation', NOW() - INTERVAL '30 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'john.smith@test.com';


-- emma.johnson: SUBSCRIBED (Silver) → UPGRADED (Gold)
INSERT INTO subscription_history (id, subscription_id, action_type, previous_tier, new_tier, reason, created_at, updated_at)
SELECT gen_random_uuid(), s.id, 'SUBSCRIBED', NULL,     'SILVER', 'Initial tier assignment',        NOW() - INTERVAL '30 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'emma.johnson@test.com'
UNION ALL
SELECT gen_random_uuid(), s.id, 'UPGRADED',   'SILVER', 'GOLD',   'Tier upgraded after evaluation', NOW() - INTERVAL '15 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'emma.johnson@test.com';


-- michael.brown: SUBSCRIBED directly at Gold (high order count from start)
INSERT INTO subscription_history (id, subscription_id, action_type, previous_tier, new_tier, reason, created_at, updated_at)
SELECT gen_random_uuid(), s.id, 'SUBSCRIBED', NULL, 'GOLD', 'Initial tier assignment', NOW() - INTERVAL '10 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'michael.brown@test.com';


-- olivia.davis: SUBSCRIBED Silver, no changes (low activity)
INSERT INTO subscription_history (id, subscription_id, action_type, previous_tier, new_tier, reason, created_at, updated_at)
SELECT gen_random_uuid(), s.id, 'SUBSCRIBED', NULL, 'SILVER', 'Initial tier assignment', NOW() - INTERVAL '5 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'olivia.davis@test.com';


-- william.miller: SUBSCRIBED Silver, no history (0 orders, nothing happened yet)
INSERT INTO subscription_history (id, subscription_id, action_type, previous_tier, new_tier, reason, created_at, updated_at)
SELECT gen_random_uuid(), s.id, 'SUBSCRIBED', NULL, 'SILVER', 'Initial tier assignment', NOW() - INTERVAL '2 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'william.miller@test.com';


-- sophia.wilson: SUBSCRIBED Silver → UPGRADED Gold (earned via 12 orders + spend)
INSERT INTO subscription_history (id, subscription_id, action_type, previous_tier, new_tier, reason, created_at, updated_at)
SELECT gen_random_uuid(), s.id, 'SUBSCRIBED', NULL,     'SILVER', 'Initial tier assignment',        NOW() - INTERVAL '45 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'sophia.wilson@test.com'
UNION ALL
SELECT gen_random_uuid(), s.id, 'UPGRADED',   'SILVER', 'GOLD',   'Tier upgraded after evaluation', NOW() - INTERVAL '20 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'sophia.wilson@test.com';


-- james.anderson: SUBSCRIBED Silver → CANCELLED
INSERT INTO subscription_history (id, subscription_id, action_type, previous_tier, new_tier, reason, created_at, updated_at)
SELECT gen_random_uuid(), s.id, 'SUBSCRIBED', NULL,     'SILVER', 'Initial tier assignment', NOW() - INTERVAL '60 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'james.anderson@test.com'
UNION ALL
SELECT gen_random_uuid(), s.id, 'CANCELLED',  'SILVER', NULL, 'Subscription cancelled',  NOW() - INTERVAL '40 days', NOW()
FROM subscriptions s JOIN users u ON s.user_id = u.id WHERE u.email = 'james.anderson@test.com';
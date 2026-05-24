-- ============================================================
-- V11: Seed Orders
--
-- Verified totals against tier rules (V2):
--   GOLD     requires: ORDER_COUNT >= 10  AND  MONTHLY_SPEND >= 5000
--   PLATINUM requires: ORDER_COUNT >= 25  AND  MONTHLY_SPEND >= 15000
--                      AND COHORT = PREMIUM_USERS
--
-- User breakdown:
--   john.smith      → 30 orders, ₹28,500 spend  → GOLD ✓  PLATINUM ✓ (+ PREMIUM_USERS cohort)
--   emma.johnson    → 12 orders, ₹7,750  spend  → GOLD ✓  PLATINUM ✗ (cohort ok but spend < 15000)
--   michael.brown   → 15 orders, ₹9,900  spend  → GOLD ✓  PLATINUM ✗ (REGULAR cohort blocks it)
--   olivia.davis    →  3 orders, ₹1,200  spend  → GOLD ✗  stays SILVER
--   william.miller  →  0 orders, ₹0      spend  → GOLD ✗  stays SILVER  ← DEMO EVALUATION USER
--   sophia.wilson   → 12 orders, ₹7,300  spend  → GOLD ✓  PLATINUM ✗ (no cohort)
--   james.anderson  →  5 orders, ₹2,000  spend  → GOLD ✗  (cancelled subscription anyway)
--   charlotte.taylor→  0 orders                 → no subscription yet
-- ============================================================


-- john.smith: 30 orders spread over 30 days → total ₹28,500
INSERT INTO orders (id, user_id, order_value, created_at, updated_at)
SELECT
    gen_random_uuid(),
    u.id,
    (ARRAY[750,900,1200,1500,800,650,1100,950,700,1300,
     600,850,1400,750,900,1050,800,1200,650,950,
     1100,700,850,1300,600,750,900,1500,800,1000])[gs.n],
    NOW() - (INTERVAL '1 day' * gs.n),
    NOW()
FROM users u
    CROSS JOIN generate_series(1, 30) AS gs(n)
WHERE u.email = 'john.smith@test.com';


-- emma.johnson: 12 orders spread over 24 days → total ₹7,750
INSERT INTO orders (id, user_id, order_value, created_at, updated_at)
SELECT
    gen_random_uuid(),
    u.id,
    (ARRAY[500,750,600,800,450,700,550,650,900,500,600,750])[gs.n],
    NOW() - (INTERVAL '2 days' * gs.n),
    NOW()
FROM users u
    CROSS JOIN generate_series(1, 12) AS gs(n)
WHERE u.email = 'emma.johnson@test.com';


-- michael.brown: 15 orders spread over 15 days → total ₹9,900
INSERT INTO orders (id, user_id, order_value, created_at, updated_at)
SELECT
    gen_random_uuid(),
    u.id,
    (ARRAY[600,700,500,800,550,650,750,900,450,600,700,500,800,650,750])[gs.n],
    NOW() - (INTERVAL '1 day' * gs.n),
    NOW()
FROM users u
    CROSS JOIN generate_series(1, 15) AS gs(n)
WHERE u.email = 'michael.brown@test.com';


-- olivia.davis: 3 orders → total ₹1,200 (stays Silver, below both thresholds)
INSERT INTO orders (id, user_id, order_value, created_at, updated_at)
SELECT
    gen_random_uuid(),
    u.id,
    (ARRAY[300,500,400])[gs.n],
    NOW() - (INTERVAL '3 days' * gs.n),
    NOW()
FROM users u
    CROSS JOIN generate_series(1, 3) AS gs(n)
WHERE u.email = 'olivia.davis@test.com';


-- william.miller: 0 orders intentionally
-- This is the live demo user: evaluate-tier → SILVER (no orders)
-- Then POST /orders live → evaluate-tier again → upgrades to GOLD
-- No INSERT here by design.


-- sophia.wilson: 12 orders spread over 24 days → total ₹7,300
INSERT INTO orders (id, user_id, order_value, created_at, updated_at)
SELECT
    gen_random_uuid(),
    u.id,
    (ARRAY[500,600,700,450,800,550,650,750,500,600,700,500])[gs.n],
    NOW() - (INTERVAL '2 days' * gs.n),
    NOW()
FROM users u
    CROSS JOIN generate_series(1, 12) AS gs(n)
WHERE u.email = 'sophia.wilson@test.com';


-- james.anderson: 5 orders placed before cancellation → total ₹2,000
INSERT INTO orders (id, user_id, order_value, created_at, updated_at)
SELECT
    gen_random_uuid(),
    u.id,
    (ARRAY[400,300,500,350,450])[gs.n],
    NOW() - INTERVAL '50 days' + (INTERVAL '2 days' * gs.n),
    NOW()
FROM users u
    CROSS JOIN generate_series(1, 5) AS gs(n)
WHERE u.email = 'james.anderson@test.com';


-- charlotte.taylor: 0 orders, no subscription → fresh user for live demo
-- No INSERT here by design.
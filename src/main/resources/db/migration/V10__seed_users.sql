-- ============================================================
-- V10: Seed Users
-- 8 users with deliberate cohort assignments:
--   PREMIUM_USERS → eligible for Platinum via COHORT rule
--   REGULAR       → must earn tier via orders only
--   NULL          → no cohort, pure order-based progression
-- ============================================================

INSERT INTO users (id, email, full_name, cohort, created_at, updated_at)
VALUES (gen_random_uuid(), 'john.smith@test.com', 'John Smith', 'PREMIUM_USERS', NOW(), NOW()),
       (gen_random_uuid(), 'emma.johnson@test.com', 'Emma Johnson', 'PREMIUM_USERS', NOW(), NOW()),
       (gen_random_uuid(), 'michael.brown@test.com', 'Michael Brown', 'REGULAR', NOW(), NOW()),
       (gen_random_uuid(), 'olivia.davis@test.com', 'Olivia Davis', 'REGULAR', NOW(), NOW()),
       (gen_random_uuid(), 'william.miller@test.com', 'William Miller', NULL, NOW(), NOW()),
       (gen_random_uuid(), 'sophia.wilson@test.com', 'Sophia Wilson', NULL, NOW(), NOW()),
       (gen_random_uuid(), 'james.anderson@test.com', 'James Anderson', 'REGULAR', NOW(), NOW()),
       (gen_random_uuid(), 'charlotte.taylor@test.com', 'Charlotte Taylor', NULL, NOW(), NOW());
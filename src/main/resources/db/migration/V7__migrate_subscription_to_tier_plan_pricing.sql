--Add new columns
ALTER TABLE subscriptions
    ADD COLUMN tier_plan_pricing_id UUID;

ALTER TABLE subscriptions
    ADD COLUMN amount_paid NUMERIC(10, 2);


--Backfill existing subscription records
UPDATE subscriptions s
SET tier_plan_pricing_id = tpp.id,
    amount_paid          = tpp.price
FROM tier_plan_pricing tpp
WHERE s.current_tier_id = tpp.membership_tier_id
  AND s.membership_plan_id = tpp.membership_plan_id;


--Add foreign key constraint
ALTER TABLE subscriptions
    ADD CONSTRAINT fk_subscriptions_tier_plan_pricing
        FOREIGN KEY (tier_plan_pricing_id)
            REFERENCES tier_plan_pricing (id);


-- Make columns mandatory
ALTER TABLE subscriptions
    ALTER COLUMN tier_plan_pricing_id SET NOT NULL;

ALTER TABLE subscriptions
    ALTER COLUMN amount_paid SET NOT NULL;
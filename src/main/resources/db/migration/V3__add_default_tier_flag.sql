ALTER TABLE membership_tiers
    ADD COLUMN default_tier BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE membership_tiers
SET default_tier = TRUE
WHERE tier_type = 'SILVER';

CREATE UNIQUE INDEX uq_default_tier
    ON membership_tiers(default_tier)
    WHERE default_tier = TRUE;
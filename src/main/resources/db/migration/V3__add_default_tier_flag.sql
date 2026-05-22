ALTER TABLE membership_tier
    ADD COLUMN default_tier BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE membership_tier
SET default_tier = TRUE
WHERE tier_type = 'SILVER';

CREATE UNIQUE INDEX uq_default_tier
    ON membership_tier(default_tier)
    WHERE default_tier = TRUE;
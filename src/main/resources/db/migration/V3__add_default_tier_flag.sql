ALTER TABLE membership_tier
    ADD COLUMN default_tier BOOLEAN NOT NULL DEFAULT FALSE;


UPDATE membership_tier
SET default_tier = TRUE
WHERE tier_type = 'SILVER';
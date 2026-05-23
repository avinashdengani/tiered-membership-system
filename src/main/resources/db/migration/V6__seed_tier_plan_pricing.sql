INSERT INTO tier_plan_pricing (id,
                               membership_tier_id,
                               membership_plan_id,
                               price,
                               currency,
                               active,
                               created_at,
                               updated_at)

SELECT gen_random_uuid(),
       mt.id,
       mp.id,

       CASE

           -- SILVER Pricing
           WHEN mt.tier_type = 'SILVER'
               AND mp.plan_type = 'MONTHLY'
               THEN 199

           WHEN mt.tier_type = 'SILVER'
               AND mp.plan_type = 'QUARTERLY'
               THEN 499

           WHEN mt.tier_type = 'SILVER'
               AND mp.plan_type = 'YEARLY'
               THEN 1999


           -- GOLD Pricing
           WHEN mt.tier_type = 'GOLD'
               AND mp.plan_type = 'MONTHLY'
               THEN 499

           WHEN mt.tier_type = 'GOLD'
               AND mp.plan_type = 'QUARTERLY'
               THEN 1399

           WHEN mt.tier_type = 'GOLD'
               AND mp.plan_type = 'YEARLY'
               THEN 4999


           -- PLATINUM Pricing
           WHEN mt.tier_type = 'PLATINUM'
               AND mp.plan_type = 'MONTHLY'
               THEN 799

           WHEN mt.tier_type = 'PLATINUM'
               AND mp.plan_type = 'QUARTERLY'
               THEN 2199

           WHEN mt.tier_type = 'PLATINUM'
               AND mp.plan_type = 'YEARLY'
               THEN 7999
           END,

       'INR',
       TRUE,
       NOW(),
       NOW()

FROM membership_tiers mt
         CROSS JOIN membership_plans mp

WHERE mt.tier_type IN ('SILVER', 'GOLD', 'PLATINUM')
  AND mp.plan_type IN ('MONTHLY', 'QUARTERLY', 'YEARLY');
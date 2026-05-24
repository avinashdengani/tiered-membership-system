<div align="center">

# 🏆 Tiered Membership System

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL_16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

**A fully working backend engine for subscription memberships — built for a commerce platform.**  
Subscribe, upgrade, downgrade, cancel, and auto-evaluate tiers based on real purchase behaviour.

Everything is database-driven. New tier? Insert a row. New rule? One class. Zero redeploys.

</div>

---

## ✨ What This Does

A user lands on the platform and decides to subscribe.

1. They pick a **Plan** (Monthly / Quarterly / Yearly) and a **Tier** (Silver / Gold / Platinum)
2. They subscribe → active subscription is created, history recorded, benefits returned
3. They shop. Orders accumulate.
4. Someone calls **Evaluate Tier** → the rule engine checks their order count, monthly spend, and cohort automatically
5. If they cross a threshold → tier upgrades. History recorded. New benefits applied.
6. They can also manually upgrade, downgrade, or cancel at any time.

That's the whole system. Let's see how it's built.

---

## 🏗️ Architecture

```
┌───────────────────────────────────────────────────────────────────┐
│                          HTTP / REST                              │
│          Swagger UI  ·  Postman  ·  Any HTTP client               │
└───────────────────────┬───────────────────────────────────────────┘
                        │
┌───────────────────────▼───────────────────────────────────────────┐
│                       Controllers (5)                             │
│                                                                   │
│  /api/v1/subscriptions      SubscriptionController                │
│  /api/v1/subscriptions/plans   MembershipPlanController           │
│  /api/v1/subscriptions/tiers   MembershipTierController           │
│  /api/v1/subscriptions/pricing TierPlanPricingController          │
│  /api/v1/users/{id}/subscription  UserController                  │
└──────┬──────────────────────────────────────┬─────────────────────┘
       │                                      │
┌──────▼────────────────┐        ┌────────────▼──────────────────────┐
│    SubscriptionService │        │      TierEvaluationService        │
│  ─────────────────────│        │  ─────────────────────────────────│
│  · subscribe           │        │  · evaluateTier(subscriptionId)   │
│  · cancel              │        │       ↓                           │
│  · upgrade             │        │  Load all active TierRules         │
│  · downgrade           │        │  Group by MembershipTier           │
│  · getByUser           │        │  For each tier, evaluate ALL rules │
│  · resolveExpiry       │        │  Pick highest eligible tier        │
│    (lazy expiry check) │        │  Update subscription if changed    │
└──────────┬────────────┘        └────────────┬──────────────────────┘
           │                                  │
           └──────────────┬───────────────────┘
                          │
              ┌───────────▼───────────────────────────────┐
              │         Rule Engine                        │
              │                                            │
              │  TierRuleEvaluator (interface)             │
              │  ├── OrderCountRuleEvaluator               │
              │  │     countByUserId(userId)               │
              │  ├── MonthlySpendRuleEvaluator             │
              │  │     sumOrderValueSince(userId, 30d)     │
              │  └── CohortRuleEvaluator                   │
              │        user.getCohort() == ruleValue       │
              │                                            │
              │  OperatorEvaluator (interface)             │
              │  ├── GREATER_THAN / GREATER_THAN_EQUAL     │
              │  ├── LESS_THAN / LESS_THAN_EQUAL           │
              │  └── EQUALS                                │
              └───────────────────────────────────────────┘
                          │
              ┌───────────▼───────────────────────────────┐
              │         PostgreSQL 16                      │
              │   10 tables · 12 Flyway migrations        │
              │   Seeded · ready to demo immediately       │
              └───────────────────────────────────────────┘
```

---

## 📐 Domain Model

The schema is designed around two key decisions:

**Decision 1 — Pricing is a first-class entity, not a formula.**
`tier_plan_pricing` stores one row per (tier × plan) combination. Price changes are a DB update. No code touched.

**Decision 2 — Benefits are configurable, not hardcoded.**
The `benefits` table stores a JSONB `configuration` column. `{"slaHours": 2}` for Priority Support. `{"enabled": true}`
for Free Delivery. Business can change benefit config without engineering.

<div align="center">
  <img src="\docs\architecture\db-schema.svg" alt="Database Schema Diagram" width="80%" />
</div>

---

## 🔑 The Rule Engine (The interesting part)

This is what makes the system extensible. The assignment asked: *"Users move through tiers based on criteria — should be
configurable."*

Here's how it works:

```java
// TierRuleEvaluator — one interface, three implementations today
public interface TierRuleEvaluator {
    RuleType getSupportedRuleType();

    boolean evaluate(Subscription subscription, TierRule rule);
}
```

Each `TierRule` row in the DB looks like:

| Tier     | Rule Type     | Operator           | Value         |
|----------|---------------|--------------------|---------------|
| GOLD     | ORDER_COUNT   | GREATER_THAN_EQUAL | 10            |
| GOLD     | MONTHLY_SPEND | GREATER_THAN_EQUAL | 5000          |
| PLATINUM | ORDER_COUNT   | GREATER_THAN_EQUAL | 25            |
| PLATINUM | MONTHLY_SPEND | GREATER_THAN_EQUAL | 15000         |
| PLATINUM | COHORT        | EQUALS             | PREMIUM_USERS |

At evaluation time:

- Rules are grouped by tier
- **ALL rules for a tier must pass** (AND logic)
- The **highest eligible tier wins**
- Silver is the default (no rules required)

To add a new rule type tomorrow: write one class, annotate `@Component`. Spring auto-discovers it. Done.

---

## 💰 Pricing Matrix (seeded, ready to test)

| Tier     | Monthly | Quarterly | Yearly |
|----------|---------|-----------|--------|
| Silver   | ₹199    | ₹499      | ₹1,999 |
| Gold     | ₹499    | ₹1,399    | ₹4,999 |
| Platinum | ₹799    | ₹2,199    | ₹7,999 |

---

## 🎁 Benefits by Tier (seeded)

| Benefit          | Silver | Gold | Platinum |
|------------------|--------|------|----------|
| Free Delivery    | ✅      | ✅    | ✅        |
| Early Access     | ❌      | ✅    | ✅        |
| Priority Support | ❌      | ❌    | ✅        |

---

## 📡 API Reference

| Method | Endpoint                                               | What it does                                        |
|--------|--------------------------------------------------------|-----------------------------------------------------|
| `GET`  | `/api/v1/subscriptions/plans`                          | List all active membership plans                    |
| `GET`  | `/api/v1/subscriptions/tiers`                          | List all tiers with their benefits                  |
| `GET`  | `/api/v1/subscriptions/pricing`                        | Full pricing matrix (all tier × plan combos)        |
| `POST` | `/api/v1/subscriptions`                                | Create subscription (userId + planType + tierType)  |
| `GET`  | `/api/v1/subscriptions/{subscriptionId}`               | Get subscription details + current benefits         |
| `GET`  | `/api/v1/users/{userId}/subscription`                  | Get active subscription by user (lazy expiry check) |
| `PUT`  | `/api/v1/subscriptions/{subscriptionId}/upgrade`       | Manual tier upgrade (`?newTierType=GOLD`)           |
| `PUT`  | `/api/v1/subscriptions/{subscriptionId}/downgrade`     | Manual tier downgrade (`?newTierType=SILVER`)       |
| `PUT`  | `/api/v1/subscriptions/{subscriptionId}/cancel`        | Cancel active subscription                          |
| `GET`  | `/api/v1/subscriptions/{subscriptionId}/history`       | Full audit trail of all state changes               |
| `POST` | `/api/v1/subscriptions/{subscriptionId}/evaluate-tier` | Trigger rule-based tier evaluation                  |

---

## 🔒 Concurrency Safety

Two concurrent requests try to subscribe the same user. Here's what stops it:

**Layer 1 — Application guard**
`existsByUserIdAndStatus(userId, ACTIVE)` runs inside `@Transactional`. If the check passes, no active subscription
exists yet.

**Layer 2 — Optimistic locking**
Every entity carries a `@Version` column. If two transactions race, one gets a `StaleObjectStateException` on save.
Spring rolls back the loser.

This is intentional. A database-level serialisation is the right tool here — no Redis, no distributed locks, no
over-engineering.

---

## 🚀 Quick Start

```bash
# 1. Start Postgres
docker-compose up -d

# 2. Run — Flyway applies all 12 migrations + seeds data automatically
./mvnw spring-boot:run

# 3. Hit Swagger
open http://localhost:8080/swagger-ui.html
```

Pre-seeded across 12 Flyway migrations: **8 users** (with deliberate cohort assignments), **77 orders** across 6 of
them, **3 plans**, **3 tiers**, **5 tier rules**, **3 benefits**, **6 tier-benefit mappings**, **9 pricing rows** (3
tiers × 3 plans), **7 subscriptions** (6 active + 1 cancelled), and **12 subscription history entries** capturing full
lifecycle stories. Start calling APIs immediately.

---

## ⏱️ Expiry Handling

Subscription expiry is resolved **lazily on read** — no background process required to keep state correct.

Both fetch endpoints check expiry at call time:

- `GET /api/v1/subscriptions/{subscriptionId}` — checks and marks expired before returning
- `GET /api/v1/users/{userId}/subscription` — same lazy check on every call

If a subscription is past its `expiryDate`, it is marked `EXPIRED`, saved, and recorded in `subscription_history` — all
within the same transaction. The caller always gets the true current state, not a stale one.

This is a deliberate tradeoff: simple, zero-infrastructure, and correct for the current scale. The natural evolution is
a scheduler that proactively sweeps expired subscriptions in bulk (see Future Scope).

---

## 🔮 What's Next (Future Scope)

| What                               | Why it matters                                                                                                                                                                                                                                                                                     |
|------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Expiry Scheduler**               | Currently, subscription expiry is checked lazily when a user fetches their subscription. A `@Scheduled` job scanning `status = ACTIVE AND expiry_date < NOW()` every hour would mark expired subscriptions proactively — enabling renewal nudges, accurate reporting, and clean status everywhere. |
| **Tier Auto-Evaluation Scheduler** | Instead of manually calling `evaluate-tier`, a scheduled job could run evaluation for all active subscriptions nightly — automating tier promotions based on real purchase behaviour without any API call.                                                                                         |
| **Payment Integration**            | Hook into a payment gateway at subscription creation. Record `transactionId` on the `Subscription` entity. Block subscription activation until payment confirms.                                                                                                                                   |
| **Renewal Flow**                   | Auto-renew subscriptions before expiry. Support configurable grace periods. Log renewal in `subscription_history`.                                                                                                                                                                                 |
| **Domain Events**                  | Publish `TierUpgradedEvent`, `SubscriptionExpiredEvent` etc. Downstream services (delivery engine, discount service) react without coupling to this service.                                                                                                                                       |
| **Admin APIs**                     | CRUD for plans, tiers, rules, and benefits — so business teams change configuration without touching the DB directly.                                                                                                                                                                              |
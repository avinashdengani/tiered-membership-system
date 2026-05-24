package com.quickcom.membership.exception;

public final class ExceptionMessages {

    public static final String SUBSCRIPTION_NOT_FOUND = "Subscription not found";
    public static final String ACTIVE_SUBSCRIPTION_NOT_FOUND = "No active subscription found";
    public static final String SUBSCRIPTION_ALREADY_CANCELLED = "Subscription already cancelled";
    public static final String DUPLICATE_ACTIVE_SUBSCRIPTION = "Active subscription already exists";

    public static final String MEMBERSHIP_PLAN_NOT_FOUND = "Membership plan not found";
    public static final String MEMBERSHIP_TIER_NOT_FOUND = "Membership tier not found";
    public static final String DEFAULT_TIER_NOT_CONFIGURED = "Default tier not configured";

    public static final String NEW_TIER_MUST_BE_HIGHER = "New tier must be higher than current tier";
    public static final String NEW_TIER_MUST_BE_LOWER = "New tier must be lower than current tier";

    public static final String UNSUPPORTED_RULE_TYPE = "Unsupported rule type configured";
    public static final String UNSUPPORTED_OPERATOR_TYPE = "Unsupported operator type configured";

    public static final String DATA_INTEGRITY_CONFLICT = "Request conflicts with existing data";
    public static final String CONCURRENT_UPDATE_DETECTED = "Concurrent update detected. Please retry.";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String VALIDATION_EXCEPTION = "Validation Exception";
    public static final String INVALID_PARAMETER = "Invalid value for parameter: ";

    public static final String TIER_PLAN_PRICING_NOT_FOUND = "Selected tier and plan is not available.";

    public static final String USER_NOT_FOUND = "User not found";

    private ExceptionMessages() {}
}

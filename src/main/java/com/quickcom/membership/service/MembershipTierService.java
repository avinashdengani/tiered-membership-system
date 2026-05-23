package com.quickcom.membership.service;


import com.quickcom.membership.dto.response.MembershipTierResponse;

import java.util.List;

public interface MembershipTierService {

    List<MembershipTierResponse> getAllActiveTiers();
}

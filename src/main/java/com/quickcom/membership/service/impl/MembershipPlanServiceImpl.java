package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.MembershipPlan;
import com.quickcom.membership.dto.response.MembershipPlanResponse;
import com.quickcom.membership.mapper.MembershipPlanMapper;
import com.quickcom.membership.repository.MembershipPlanRepository;
import com.quickcom.membership.service.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanMapper membershipPlanMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MembershipPlanResponse> getAllActivePlans() {

        return membershipPlanMapper.mapToResponse(membershipPlanRepository.findByActiveTrue());
    }
}

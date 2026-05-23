package com.quickcom.membership.service.impl;

import com.quickcom.membership.dto.response.MembershipTierResponse;
import com.quickcom.membership.mapper.MembershipTierMapper;
import com.quickcom.membership.repository.MembershipTierRepository;
import com.quickcom.membership.service.MembershipTierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipTierServiceImpl implements MembershipTierService {
    private final  MembershipTierMapper membershipTierMapper;
    private final MembershipTierRepository membershipTierRepository;

    @Override
    public List<MembershipTierResponse> getAllActiveTiers() {

        return membershipTierMapper.mapToResponse(
                membershipTierRepository.findAllByActiveTrueOrderByPriorityAsc()
        );
    }
}

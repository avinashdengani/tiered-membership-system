package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.dto.response.MembershipTierResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MembershipTierMapper {

    public List<MembershipTierResponse> mapToResponse(List<MembershipTier> membershipTiers) {

        return membershipTiers.stream().map(this::mapToResponse).toList();
    }

    public MembershipTierResponse mapToResponse(MembershipTier membershipTier) {

        MembershipTierResponse response = new MembershipTierResponse();

        response.setId(membershipTier.getId());
        response.setTierType(membershipTier.getTierType());
        response.setDisplayName(membershipTier.getDisplayName());
        response.setDefaultTier(membershipTier.isDefaultTier());
        response.setPriority(membershipTier.getPriority());

        return response;
    }
}
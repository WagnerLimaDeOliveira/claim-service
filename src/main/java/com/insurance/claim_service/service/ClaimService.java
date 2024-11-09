package com.insurance.claim_service.service;

import com.insurance.claim_service.entity.Claim;
import com.insurance.claim_service.repository.ClaimRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ClaimService {
    @Inject
    ClaimRepository claimRepository;

    public Claim getClaimById(Long claimId){
        return claimRepository.findById(claimId);
    }

    @Transactional
    public Claim createClaim(Claim claim){
        if(claim.getId() == null){
            claimRepository.persist(claim);
            return claim;
        } else {
            return null;
        }
    }

    @Transactional
    public Claim updateClaim(Claim claim){
        if(claim.getId() != null){
        claimRepository.getEntityManager().merge(claim);
        return claim;
        } else {
            return null;
        }
    }

    @Transactional
    public boolean deleteClaimById(Long claimId){
        return claimRepository.deleteById(claimId);
    }
}

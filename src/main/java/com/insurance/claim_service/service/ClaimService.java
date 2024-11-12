package com.insurance.claim_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.claim_service.dto.ClaimFileDTO;
import com.insurance.claim_service.entity.Claim;
import com.insurance.claim_service.entity.ClaimFile;
import com.insurance.claim_service.repository.ClaimFileRepository;
import com.insurance.claim_service.repository.ClaimRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import java.util.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ClaimService {
    @Inject
    ClaimRepository claimRepository;

    @Inject
    ClaimFileRepository claimFileRepository;

    @Inject
    ObjectMapper objectMapper;

    private static final Logger logger = Logger.getLogger(ClaimService.class.getName());


    public Claim getClaimById(Long claimId) {
        return claimRepository.findById(claimId);
    }

    public List<ClaimFile> getClaimFilesByClaimId(Long claimId){
        return claimFileRepository.find("claimId", claimId).stream().toList();
    }

    @Transactional
    public Claim createClaim(Claim claim) {
        if (claim.getId() == null) {
            claimRepository.persist(claim);
            return claim;
        } else {
            return null;
        }
    }

    @Transactional
    public Claim updateClaim(Claim claim) {
        if (claim.getId() != null) {
            claimRepository.getEntityManager().merge(claim);
            return claim;
        } else {
            return null;
        }
    }

    @Incoming("add-claim-file-events")
    @Transactional
    public void addClaimFileEvent(String claimFileJson) {
        try {
            ClaimFileDTO claimFileDTO = objectMapper.readValue(claimFileJson, ClaimFileDTO.class);
            ClaimFile claimFile = new ClaimFile();
            claimFile.setClaimId(claimFileDTO.claimId);
            claimFile.setFileName(claimFileDTO.fileName);
            claimFileRepository.persist(claimFile);
            logger.info("new claim file added: " + claimFileJson);
        } catch (JsonProcessingException e) {
            logger.severe("Failed to parse ClaimFileDTO JSON: " + claimFileJson);
        } catch (Exception e) {
            logger.severe("Failed to persist ClaimFile: " + e.getMessage());
        }
    }

    @Transactional
    public boolean deleteClaimById(Long claimId) {
        return claimRepository.deleteById(claimId);
    }
}

package com.insurance.claim_service.controller;

import com.insurance.claim_service.entity.ClaimFile;
import com.insurance.claim_service.service.ClaimService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("claim-file")
public class ClaimFileController {

    @Inject
    ClaimService claimService;

    @GET
    @Path("{claimId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClaimFile> getClaimFilesByClaimId(@PathParam("claimId") Long claimId) {
        return claimService.getClaimFilesByClaimId(claimId);
    }
}

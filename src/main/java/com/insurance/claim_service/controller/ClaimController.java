package com.insurance.claim_service.controller;

import com.insurance.claim_service.entity.Claim;
import com.insurance.claim_service.service.ClaimService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/policy")
public class ClaimController {
    @Inject
    private ClaimService claimService;

    @GET
    @Path("{policyNumber}/claim/{claimId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Claim getClaim(@PathParam("policyNumber") String policyNumber, @PathParam("claimId") Long claimId) {
        return claimService.getClaimById(claimId);
    }

    @POST
    @Path("{policyNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Claim createClaim(@PathParam("policyNumber") String policyNumber, Claim claim) {
        if (claim.getId() == null) {
            claimService.createClaim(claim);
            return claim;
        } else {
            return null;
        }
    }

    @PUT
    @Path("{policyNumber}/claim/{claimId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Claim updateClaim(@PathParam("policyNumber") String policyNumber, @PathParam("claimId") Long claimId, Claim claim) {
        if (claim.getId() != null) {
            claimService.updateClaim(claim);
            return claim;
        } else {
            return null;
        }
    }

    @DELETE
    @Path("{policyNumber}/claim/{claimId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteClaimById(@PathParam("policyNumber") String policyNumber, @PathParam("claimId") Long claimId) {
        return claimService.deleteClaimById(claimId);
    }
}

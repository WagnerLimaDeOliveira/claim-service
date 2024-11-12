package com.insurance.claim_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/*TODO: Move this to a separate library as it is used cross multiple microservices*/
public class ClaimFileDTO {
    @JsonProperty("claimId")
    public Long claimId;
    @JsonProperty("fileName")
    public String fileName;

    public ClaimFileDTO(Long claimId, String fileName){
        this.claimId = claimId;
        this.fileName = fileName;
    }
}

package com.insurance.claim_service.repository;

import com.insurance.claim_service.entity.ClaimFile;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClaimFileRepository implements PanacheRepository<ClaimFile> {
}

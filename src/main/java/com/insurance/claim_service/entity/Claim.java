package com.insurance.claim_service.entity;

import com.insurance.claim_service.dto.ClaimStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;

@Entity
public class Claim extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="policy_number", nullable = false)
    String policyNumber;

    @Column(name="date_of_loss")
    Instant dateOfLoss;

    @Column(name="date_filed", nullable = false)
    Instant dateFiled;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    ClaimStatus status;

    @Column(name="amount_requested")
    double amountRequested;

    @Column(name="amount_approved")
    double amountApproved;

    @Column(name="description_of_loss", nullable = false)
    String descriptionOfLoss;

    public Long getId() {
        return id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Instant getDateOfLoss() {
        return dateOfLoss;
    }

    public void setDateOfLoss(Instant dateOfLoss) {
        this.dateOfLoss = dateOfLoss;
    }

    public Instant getDateFiled() {
        return dateFiled;
    }

    public void setDateFiled(Instant dateFiled) {
        this.dateFiled = dateFiled;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public double getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(double amountRequested) {
        this.amountRequested = amountRequested;
    }

    public double getAmountApproved() {
        return amountApproved;
    }

    public void setAmountApproved(double amountApproved) {
        this.amountApproved = amountApproved;
    }

    public String getDescriptionOfLoss() {
        return descriptionOfLoss;
    }

    public void setDescriptionOfLoss(String descriptionOfLoss) {
        this.descriptionOfLoss = descriptionOfLoss;
    }
}

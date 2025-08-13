package com.example.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class CustomerRequest {
    private UUID id; // ✅ add this field

    public UUID getId() { // ✅ add getter
        return id;
    }

    public void setId(UUID id) { // ✅ add setter
        this.id = id;
    }
    @NotBlank
    private String name;
    @Email @NotBlank
    private String email;
    private BigDecimal annualSpend;
    private OffsetDateTime lastPurchaseDate;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getAnnualSpend() { return annualSpend; }
    public void setAnnualSpend(BigDecimal annualSpend) { this.annualSpend = annualSpend; }
    public OffsetDateTime getLastPurchaseDate() { return lastPurchaseDate; }
    public void setLastPurchaseDate(OffsetDateTime lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }
}

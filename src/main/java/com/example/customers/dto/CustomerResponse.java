package com.example.customers.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerResponse {
    private UUID id;
    private String name;
    private String email;
    private BigDecimal annualSpend;
    private OffsetDateTime lastPurchaseDate;
    private String tier;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getAnnualSpend() { return annualSpend; }
    public void setAnnualSpend(BigDecimal annualSpend) { this.annualSpend = annualSpend; }
    public OffsetDateTime getLastPurchaseDate() { return lastPurchaseDate; }
    public void setLastPurchaseDate(OffsetDateTime lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
}

package com.example.customers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    private BigDecimal annualSpend;

    private OffsetDateTime lastPurchaseDate;

    // Getters and setters
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
}

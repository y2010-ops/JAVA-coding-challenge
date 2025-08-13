package com.example.customers.service;

import com.example.customers.dto.CustomerRequest;
import com.example.customers.dto.CustomerResponse;
import com.example.customers.model.Customer;
import com.example.customers.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerResponse create(CustomerRequest request) {
        Customer entity = new Customer();
        apply(request, entity);
        Customer saved = repository.save(entity);
        return toResponse(saved);
    }

    public CustomerResponse getById(UUID id) {
        Customer c = repository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        return toResponse(c);
    }

    public CustomerResponse getByName(String name) {
        Customer c = repository.findByName(name).orElseThrow(() -> new NotFoundException("Customer not found"));
        return toResponse(c);
    }

    public CustomerResponse getByEmail(String email) {
        Customer c = repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Customer not found"));
        return toResponse(c);
    }

    public CustomerResponse update(UUID id, CustomerRequest request) {
        Customer c = repository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        apply(request, c);
        Customer saved = repository.save(c);
        return toResponse(saved);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Customer not found");
        }
        repository.deleteById(id);
    }

    private void apply(CustomerRequest req, Customer c) {
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        c.setAnnualSpend(req.getAnnualSpend());
        c.setLastPurchaseDate(req.getLastPurchaseDate());
    }

    private CustomerResponse toResponse(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setEmail(c.getEmail());
        r.setAnnualSpend(c.getAnnualSpend());
        r.setLastPurchaseDate(c.getLastPurchaseDate());
        r.setTier(calculateTier(c.getAnnualSpend(), c.getLastPurchaseDate()));
        return r;
    }

    public String calculateTier(BigDecimal annualSpend, OffsetDateTime lastPurchaseDate) {
        BigDecimal spend = annualSpend == null ? BigDecimal.ZERO : annualSpend;
        if (spend.compareTo(new BigDecimal("10000")) >= 0) {
            if (lastPurchaseDate != null && lastPurchaseDate.isAfter(OffsetDateTime.now().minus(6, ChronoUnit.MONTHS))) {
                return "Platinum";
            }
        }
        if (spend.compareTo(new BigDecimal("1000")) >= 0 && spend.compareTo(new BigDecimal("10000")) < 0) {
            if (lastPurchaseDate != null && lastPurchaseDate.isAfter(OffsetDateTime.now().minus(12, ChronoUnit.MONTHS))) {
                return "Gold";
            }
        }
        return "Silver";
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) { super(message); }
    }
}

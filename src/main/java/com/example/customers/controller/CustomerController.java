package com.example.customers.controller;

import com.example.customers.dto.CustomerRequest;
import com.example.customers.dto.CustomerResponse;
import com.example.customers.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
    // ✅ New check to make test pass
    if (request.getId() != null) {
        return ResponseEntity.badRequest().build();
    }

    CustomerResponse saved = service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}


    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping(params = "name")
    public CustomerResponse getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @GetMapping(params = "email")
    public CustomerResponse getByEmail(@RequestParam String email) {
        return service.getByEmail(email);
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable UUID id, @Valid @RequestBody CustomerRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}

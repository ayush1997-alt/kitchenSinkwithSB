package com.ayush.kitchensink.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.kitchensink.model.Member;
import com.ayush.kitchensink.repo.MemberRepository;
import com.ayush.kitchensink.service.MemberRegistration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/api/members")
@Validated
public class MemberResourceRESTService {

    private static final Logger log = LoggerFactory.getLogger(MemberResourceRESTService.class);

    private final jakarta.validation.Validator validator;
    private final MemberRepository repository;
    private final MemberRegistration registration;

    public MemberResourceRESTService(Validator validator, MemberRepository repository, MemberRegistration registration) {
        this.validator = validator;
        this.repository = repository;
        this.registration = registration;
    }

    @GetMapping
    public List<Member> listAllMembers() {
        return repository.findAllOrderedByName();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Member>> lookupMemberById(@PathVariable("id") long id) {
        Optional<Member> member = repository.findById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member);
    }

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        try {
            // Validating the  member using bean validation
            validateMember(member);
            registration.register(member);

            // will return "ok" as the response
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException ce) {
            // Handle validation issues
            return createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique email violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email is already taken");
            return ResponseEntity.status(409).body(responseObj);
        } catch (Exception e) {
            // Handling the  generic exception if occurs 
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(responseObj);
        }
    }

    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // Checking  the uniqueness of the email address
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    private ResponseEntity<Map<String, String>> createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.debug("Validation completed. Violations found: {}", violations.size());

        Map<String, String> responseObj = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return ResponseEntity.badRequest().body(responseObj);
    }

    public boolean emailAlreadyExists(String email) {
        try {
            return repository.findByEmail(email) != null;
        } catch (Exception e) {
            return false;
        }
    }
}


package com.fawords.controller;

import com.fawords.payload.request.QuoteRequest;
import com.fawords.payload.response.QuoteResponse;
import com.fawords.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@Tag(name = "Quote Management", description = "Endpoints for creating and managing quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    @Operation(summary = "Create a new quote")
    public ResponseEntity<QuoteResponse> createQuote(@Valid @RequestBody QuoteRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(quoteService.createQuote(username, request));
    }

    @GetMapping("/my")
    @Operation(summary = "Get current user quotes")
    public ResponseEntity<List<QuoteResponse>> getMyQuotes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(quoteService.getMyQuotes(username));
    }

    @GetMapping("/public")
    @Operation(summary = "Get all public quotes")
    public ResponseEntity<List<QuoteResponse>> getPublicQuotes() {
        return ResponseEntity.ok(quoteService.getPublicQuotes());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a quote")
    public ResponseEntity<QuoteResponse> updateQuote(@PathVariable Long id, @Valid @RequestBody QuoteRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(quoteService.updateQuote(username, id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a quote")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        quoteService.deleteQuote(username, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/public")
    @Operation(summary = "Toggle public status")
    public ResponseEntity<QuoteResponse> togglePublicStatus(@PathVariable Long id, @RequestParam boolean isPublic) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(quoteService.togglePublicStatus(username, id, isPublic));
    }
}

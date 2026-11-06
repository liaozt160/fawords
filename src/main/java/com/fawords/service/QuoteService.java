package com.fawords.service;

import com.fawords.entity.Quote;
import com.fawords.entity.User;
import com.fawords.payload.request.QuoteRequest;
import com.fawords.payload.response.QuoteResponse;
import com.fawords.repository.QuoteRepository;
import com.fawords.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    public QuoteService(QuoteRepository quoteRepository, UserRepository userRepository) {
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public QuoteResponse createQuote(String username, QuoteRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Quote quote = new Quote();
        mapRequestToEntity(request, quote);
        quote.setUser(user);

        Quote savedQuote = quoteRepository.save(quote);
        return mapEntityToResponse(savedQuote);
    }

    @Transactional(readOnly = true)
    public List<QuoteResponse> getMyQuotes(String username) {
        return quoteRepository.findByUserUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuoteResponse> getPublicQuotes() {
        return quoteRepository.findByIsPublicTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuoteResponse updateQuote(String username, Long quoteId, QuoteRequest request) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found with id: " + quoteId));

        if (!quote.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: You do not own this quote");
        }

        mapRequestToEntity(request, quote);
        Quote updatedQuote = quoteRepository.save(quote);
        return mapEntityToResponse(updatedQuote);
    }

    @Transactional
    public void deleteQuote(String username, Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found with id: " + quoteId));

        if (!quote.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: You do not own this quote");
        }

        quoteRepository.delete(quote);
    }

    @Transactional
    public QuoteResponse togglePublicStatus(String username, Long quoteId, boolean isPublic) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found with id: " + quoteId));

        if (!quote.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: You do not own this quote");
        }

        quote.setPublic(isPublic);
        Quote updatedQuote = quoteRepository.save(quote);
        return mapEntityToResponse(updatedQuote);
    }

    private void mapRequestToEntity(QuoteRequest request, Quote quote) {
        quote.setContent(request.getContent());
        quote.setOriginal(request.isOriginal());
        quote.setAuthor(request.getAuthor());
        quote.setSourceType(request.getSourceType());
        quote.setSourceName(request.getSourceName());
        quote.setExplanation(request.getExplanation());
        quote.setCategory(request.getCategory());
        quote.setPublic(request.isPublic());
    }

    private QuoteResponse mapEntityToResponse(Quote quote) {
        QuoteResponse response = new QuoteResponse();
        response.setId(quote.getId());
        response.setContent(quote.getContent());
        response.setOriginal(quote.isOriginal());
        response.setAuthor(quote.getAuthor());
        response.setSourceType(quote.getSourceType());
        response.setSourceName(quote.getSourceName());
        response.setExplanation(quote.getExplanation());
        response.setCategory(quote.getCategory());
        response.setPublic(quote.isPublic());
        response.setCreatorUsername(quote.getUser().getUsername());
        response.setCreatedAt(quote.getCreatedAt());
        response.setUpdatedAt(quote.getUpdatedAt());
        return response;
    }
}

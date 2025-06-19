package com.sporty.application.service;

import com.sporty.domain.Ticket;
import com.sporty.adapter.out.persistence.TicketRepository;
import com.sporty.domain.TicketStatus;
import com.sporty.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sporty.utils.AuthUtils.getUserRole;

/**
 * Service for ticket management operations
 */
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    /**
     * Creates a new ticket with the given subject and description
     *
     * @param subject The ticket's subject
     * @param description The ticket's detailed description
     * @return The created Ticket entity
     */
    public Ticket createTicket(String subject, String description) {
        return ticketRepository.save(Ticket.builder()
                .subject(subject)
                .description(description)
                .status(TicketStatus.OPEN)
                .build());
    }

    /**
     * Retrieves tickets for the currently authenticated user
     *
     * @return List of tickets associated with the current user based on their role
     */
    public List<Ticket> findTicketsForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID principalId = (UUID) auth.getPrincipal();
        UserRole role = getUserRole(auth);

        return role == UserRole.AGENT
                ? findTicketsForAgent(principalId)
                : findTicketsForUser(principalId);
    }

    /**
     * Finds tickets created by a specific user
     *
     * @param userId The UUID of the user
     * @return List of tickets created by the specified user
     */
    private List<Ticket> findTicketsForUser(UUID userId) {
        return ticketRepository.findByUserId(userId);
    }

    /**
     * Finds tickets assigned to a specific agent
     *
     * @param agentId The UUID of the agent
     * @return List of tickets assigned to the specified agent
     */
    private List<Ticket> findTicketsForAgent(UUID agentId) {
        return ticketRepository.findByAssigneeId(agentId);
    }
}

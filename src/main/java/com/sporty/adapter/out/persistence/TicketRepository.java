package com.sporty.adapter.out.persistence;

import com.sporty.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Ticket entity operations
 */
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    /**
     * Finds tickets created by a specific user
     *
     * @param userId The UUID of the user who created the tickets
     * @return List of tickets created by the specified user
     */
    List<Ticket> findByUserId(UUID userId);

    /**
     * Finds tickets assigned to a specific agent
     *
     * @param assigneeId The UUID of the agent assigned to the tickets
     * @return List of tickets assigned to the specified agent
     */
    List<Ticket> findByAssigneeId(UUID assigneeId);
}

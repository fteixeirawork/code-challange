package com.sporty.adapter.in.web;

import com.sporty.adapter.in.web.schema.TicketSchema;
import com.sporty.application.service.TicketService;
import com.sporty.domain.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for ticket management operations
 */
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketRestController {

    private final TicketService ticketService;

    /**
     * Creates a new ticket with the provided information
     *
     * @param request DTO containing ticket creation details
     * @return ResponseEntity containing the newly created ticket
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketSchema ticketSchema) {
        Ticket ticket = ticketService.createTicket(ticketSchema.subject(), ticketSchema.description());

        /*
        * Should be like the following if there was a getById endpoint:
        * return ResponseEntity.created(URI.create("/tickets/" + ticket.getId())).body(ticket);
        * However, since we don't have a getById endpoint in this example, we return
        * the created ticket directly.
        * */
        return ResponseEntity.ok(ticket);
    }

    /**
     * Retrieves tickets for the currently authenticated user
     *
     * @return ResponseEntity containing a list of tickets for the current user
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Ticket>> listTickets() {
        List<Ticket> tickets = ticketService.findTicketsForCurrentUser();

        return ResponseEntity.ok(tickets);
    }
}

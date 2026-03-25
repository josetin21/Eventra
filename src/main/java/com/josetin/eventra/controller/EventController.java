package com.josetin.eventra.controller;

import com.josetin.eventra.dto.request.EventRequest;
import com.josetin.eventra.dto.response.EventResponse;
import com.josetin.eventra.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventResponse>> getPendingEvents(){
        return ResponseEntity.ok(eventService.getPendingEvents());
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponse>> getMyEvents(){
        return ResponseEntity.ok(eventService.getMyEvents());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> approveEvent(@PathVariable Long id){
        return ResponseEntity.ok(eventService.approveEvent(id));
    }
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> rejectEvent(
            @PathVariable Long id,
            @RequestParam String reason){
        return ResponseEntity.ok(eventService.rejectEvent(id, reason));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvent(){
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id){
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest request){
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> cancelEvent(@PathVariable Long id){
        eventService.cancelEvent(id);
        return ResponseEntity.ok("Event cancelled successfully");
    }
}

package com.josetin.eventra.controller;

import com.josetin.eventra.dto.request.OrganizerRequestCreate;
import com.josetin.eventra.dto.response.OrganizerRequestResponse;
import com.josetin.eventra.service.OrganizerRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizer-requests")
@RequiredArgsConstructor
public class OrganizerRequestController {

    private final OrganizerRequestService organizerRequestService;

    @PostMapping
    public ResponseEntity<OrganizerRequestResponse> submitRequest(
            @Valid @RequestBody OrganizerRequestCreate request
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizerRequestService.submitRequest(request));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrganizerRequestResponse>> getPendingRequests(){
        return ResponseEntity.ok(organizerRequestService.getPendingRequests());
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrganizerRequestResponse>> getAllRequest(){
        return ResponseEntity.ok(organizerRequestService.getAllRequests());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrganizerRequestResponse> approveRequest(@PathVariable Long id){
        return ResponseEntity.ok(organizerRequestService.approveRequest(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrganizerRequestResponse> rejectRequest(
            @PathVariable Long id,
            @RequestParam String reason
    ){
        return ResponseEntity.ok(organizerRequestService.rejectRequest(id,reason));
    }
}

package com.vecanhac.ddd.controller.admin;

import com.vecanhac.ddd.application.dto.event.EventDetailDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.event.myevents.PatchUpdateEventDTO;
import com.vecanhac.ddd.application.service.admin.AdminAppService;
import com.vecanhac.ddd.application.service.event.EventAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventAppService eventAppService;
    private final AdminAppService adminAppService;

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(
            @RequestParam(name = "status", defaultValue = "ALL") String status) {
        return ResponseEntity.ok(adminAppService.getAllEventsForAdmin(status));
    }

    @GetMapping("/search/{eventId}")
    public ResponseEntity<EventDetailDTO> getEventDetail(
            @PathVariable(name = "eventId") Long eventId) {
        return ResponseEntity.ok(adminAppService.getEventDetailAsAdmin(eventId));
    }

    @PatchMapping("/update/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEventAsAdmin(
            @PathVariable(name = "eventId") Long eventId,
            @RequestBody PatchUpdateEventDTO request) {
        return ResponseEntity.ok(adminAppService.patchUpdateEventAsAdmin(eventId, request));
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Void> deleteEventAsAdmin(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.deleteEventAsAdmin(eventId);
        return ResponseEntity.noContent().build();
    }


}
package com.vecanhac.ddd.controller.admin;

import com.vecanhac.ddd.domain.event.AdminEventSearchCriteria;
import com.vecanhac.ddd.application.dto.admin.AdminEventSummaryDTO;
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
    public ResponseEntity<List<AdminEventSummaryDTO>> searchEvents(AdminEventSearchCriteria criteria) {
        return ResponseEntity.ok(adminAppService.searchEvents(criteria));
    }




    @GetMapping("/get/{eventId}")
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



    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<String> publishEvent(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.changeEventStatus(eventId, "PUBLISHED");
        return ResponseEntity.ok("Sự kiện đã được công bố");
    }

    @PatchMapping("/{eventId}/draft")
    public ResponseEntity<String> draftEvent(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.changeEventStatus(eventId, "DRAFT");
        return ResponseEntity.ok("Sự kiện đã được chuyển về bản nháp");
    }

    @PatchMapping("/{eventId}/pending")
    public ResponseEntity<String> pendingEvent(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.changeEventStatus(eventId, "PENDING");
        return ResponseEntity.ok("Sự kiện đã được chuyển về trạng thái chờ duyệt");
    }

    @PatchMapping("/{eventId}/approve")
    public ResponseEntity<String> approveEvent(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.changeEventStatus(eventId, "PUBLISHED"); // hoặc APPROVED nếu enum khác
        return ResponseEntity.ok("Sự kiện đã được duyệt");
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<String> rejectEvent(
            @PathVariable(name = "eventId") Long eventId) {
        adminAppService.changeEventStatus(eventId, "REJECT");
        return ResponseEntity.ok("Sự kiện đã bị từ chối");
    }


}
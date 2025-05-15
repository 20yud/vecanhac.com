package com.vecanhac.ddd.controller.event.organizer;

import com.vecanhac.ddd.application.dto.event.CreateEventRequestDTO;
import com.vecanhac.ddd.application.dto.event.EventResponseDTO;
import com.vecanhac.ddd.application.dto.event.MyEventSearchCriteria;
import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizer/events")
@RequiredArgsConstructor
public class EventOrganizerController {

    private final EventAppService eventAppService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(
            @Valid @RequestBody CreateEventRequestDTO request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        request.setOrganizerId(user.getId());
        EventResponseDTO response = eventAppService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<EventResponseDTO> getMyEvents(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "time", required = false) String time,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        MyEventSearchCriteria criteria = new MyEventSearchCriteria();
        criteria.setKeyword(keyword);
        criteria.setStatus(status);
        criteria.setTime(time);

        return eventAppService.getMyEvents(user.getId(), criteria);
    }

}


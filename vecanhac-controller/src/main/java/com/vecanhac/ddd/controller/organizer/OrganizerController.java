package com.vecanhac.ddd.controller.organizer;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizer")
@PreAuthorize("hasRole('ORG') or hasRole('ADMIN')")
public class OrganizerController {


    @GetMapping("/only")
    public ResponseEntity<?> testOrganizerAccess() {
        return ResponseEntity.ok("Bạn là ORG");
    }
}

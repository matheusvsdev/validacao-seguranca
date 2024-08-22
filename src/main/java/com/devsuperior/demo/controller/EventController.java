package com.devsuperior.demo.controller;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable) {
        Page<EventDTO> eventDTOPage = service.findAll(pageable);
        return ResponseEntity.ok(eventDTOPage);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PostMapping
    public ResponseEntity<EventDTO> insertEvent(@Valid @RequestBody EventDTO eventDTO) {
        eventDTO = service.createEvent(eventDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eventDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(eventDTO);
    }
}

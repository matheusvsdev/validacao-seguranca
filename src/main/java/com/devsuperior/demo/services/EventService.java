package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repository.CityRepository;
import com.devsuperior.demo.repository.EventRepository;
import com.devsuperior.demo.services.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {

        if (eventDTO.getCityId() == null) {
            throw new ResourceNotFound("Campo cidade requerido");
        }

        authService.validateSelfOrAdmin(eventDTO.getCityId());

        if (eventDTO.getName() == null || eventDTO.getName().trim().isEmpty()) {
            throw new ResourceNotFound("Campo nome requerido");
        }

        if (eventDTO.getDate() == null || eventDTO.getDate().isBefore(LocalDate.now())) {
            throw new ResourceNotFound("Data do evento não pode ser no passado");
        }

        City city = cityRepository.findById(eventDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFound("City not found"));

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setUrl(eventDTO.getUrl());
        event.setCity(city);

        event = repository.save(event);

        return new EventDTO(event);

    }

    @Transactional(readOnly = true)
    public Page<EventDTO> findAll(Pageable pageable) {
        Page<Event> eventPage = repository.findAll(pageable);
        return eventPage.map(EventDTO::new);
    }
}

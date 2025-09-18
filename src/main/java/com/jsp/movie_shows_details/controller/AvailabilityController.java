package com.jsp.movie_shows_details.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.movie_shows_details.dto.AvailabilityResponse;
import com.jsp.movie_shows_details.service.AvailabilityService;

@RestController
@RequestMapping("/api/shows")
public class AvailabilityController {
	private final AvailabilityService service;

    public AvailabilityController(AvailabilityService service) {
        this.service = service;
    }
    @GetMapping("/{showId}/availability")
    public AvailabilityResponse getAvailability(@PathVariable Integer showId) {
        return service.getAvailability(showId);
    }

}

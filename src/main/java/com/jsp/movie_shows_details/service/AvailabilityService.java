package com.jsp.movie_shows_details.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jsp.movie_shows_details.dto.AvailabilityResponse;
import com.jsp.movie_shows_details.entity.Seat;
import com.jsp.movie_shows_details.entity.Show;
import com.jsp.movie_shows_details.repository.BookingSeatRepository;
import com.jsp.movie_shows_details.repository.SeatRepository;
import com.jsp.movie_shows_details.repository.ShowRepository;

@Service
public class AvailabilityService {
	private final ShowRepository showRepo;
    private final SeatRepository seatRepo;
    private final BookingSeatRepository bookingSeatRepo;
    
    public AvailabilityService(ShowRepository showRepo, SeatRepository seatRepo, BookingSeatRepository bookingSeatRepo) {
        this.showRepo = showRepo;
        this.seatRepo = seatRepo;
        this.bookingSeatRepo = bookingSeatRepo;
    }
    
    public AvailabilityResponse getAvailability(Integer showId) {
        Show show = showRepo.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));

        // All seats of the screen
        List<Seat> allSeats = seatRepo.findAll()
                .stream()
                .filter(s -> s.getScreen().getScreenId().equals(show.getScreen().getScreenId()))
                .collect(Collectors.toList());

        int totalSeats = allSeats.size();

        // Booked seatIds for this show
        Set<Integer> bookedSeatIds = bookingSeatRepo.findAll().stream()
                .filter(bs -> bs.getBooking().getShow().getShowId().equals(showId))
                .map(bs -> bs.getSeat().getSeatId())
                .collect(Collectors.toSet());

        int bookedSeats = bookedSeatIds.size();
        int availableSeats = totalSeats - bookedSeats;

        // Group by seatType
        Map<String, List<Seat>> seatsByType = allSeats.stream().collect(Collectors.groupingBy(Seat::getSeatType));

        Map<String, AvailabilityResponse.SeatStats> breakdown = new HashMap<>();
        for (Map.Entry<String, List<Seat>> entry : seatsByType.entrySet()) {
            String type = entry.getKey();
            List<Seat> seats = entry.getValue();
            int typeTotal = seats.size();
            int typeBooked = (int) seats.stream().filter(s -> bookedSeatIds.contains(s.getSeatId())).count();
            breakdown.put(type, new AvailabilityResponse.SeatStats(typeTotal, typeBooked));
        }

        AvailabilityResponse resp = new AvailabilityResponse();
        resp.setShowId(show.getShowId());
        resp.setShowName(show.getMovieName());
        resp.setScreenName(show.getScreen().getScreenName());
        resp.setTotalSeats(totalSeats);
        resp.setBookedSeats(bookedSeats);
        resp.setAvailableSeats(availableSeats);
        resp.setSeatBreakdown(breakdown);

        return resp;
    }

	

}

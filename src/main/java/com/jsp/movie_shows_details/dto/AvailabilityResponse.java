package com.jsp.movie_shows_details.dto;

import java.util.Map;

import lombok.Data;
@Data
public class AvailabilityResponse {
	private Integer showId;
    private String showName;
    private String screenName;
    private Integer totalSeats;
    private Integer bookedSeats;
    private Integer availableSeats;
    private Map<String, SeatStats> seatBreakdown;

    // inner static class for seat type breakdown
    public static class SeatStats {
        private Integer total;
        private Integer booked;
        private Integer available;

        public SeatStats(Integer total, Integer booked) {
            this.total = total;
            this.booked = booked;
            this.available = total - booked;
        }

        // getters
        public Integer getTotal() { return total; }
        public Integer getBooked() { return booked; }
        public Integer getAvailable() { return available; }
    }


}

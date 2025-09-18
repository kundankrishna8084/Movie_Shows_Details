package com.jsp.movie_shows_details.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "seat")
public class Seat {
	
	@Id
    private Integer seatId;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;

    private String seatNumber;
    private String seatType; // REGULAR or VIP

}

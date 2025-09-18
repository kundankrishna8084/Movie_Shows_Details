package com.jsp.movie_shows_details.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "booking")
public class Booking {
	 @Id
	    private Integer bookingId;

	    private Integer userId;

	    @ManyToOne
	    @JoinColumn(name = "show_id")
	    private Show show;

	    private String bookingStatus; // CONFIRMED, CANCELLED, etc.

	    private LocalDateTime createdAt;

}

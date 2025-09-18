package com.jsp.movie_shows_details.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.movie_shows_details.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}

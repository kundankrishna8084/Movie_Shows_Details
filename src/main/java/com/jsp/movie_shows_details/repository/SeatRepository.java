package com.jsp.movie_shows_details.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.movie_shows_details.entity.Seat;

public interface SeatRepository  extends JpaRepository<Seat, Integer>{

}

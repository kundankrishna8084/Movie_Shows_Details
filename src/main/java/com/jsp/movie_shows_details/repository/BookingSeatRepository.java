package com.jsp.movie_shows_details.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.movie_shows_details.entity.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Integer> {

}

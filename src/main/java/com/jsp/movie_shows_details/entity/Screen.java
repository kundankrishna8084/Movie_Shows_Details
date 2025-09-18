package com.jsp.movie_shows_details.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "screen")
public class Screen {
	@Id
    private Integer screenId;

    private String screenName;
    private Integer capacity;

}

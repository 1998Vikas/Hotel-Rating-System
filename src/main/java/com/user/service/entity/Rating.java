package com.user.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rating {
    private String ratingId;
    private String UserId;
    private String hotelId;
    private int rating;
    private String feedback;
    private  Hotel hotel;
}

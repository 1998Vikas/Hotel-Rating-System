package com.user.service.service.impl;

import com.user.service.entity.Hotel;
import com.user.service.entity.Rating;
import com.user.service.entity.User;
import com.user.service.exception.ResourceNotFoundException;
import com.user.service.repository.UserRepository;
import com.user.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        String string = UUID.randomUUID().toString();
        user.setUserId(string);
        User save = userRepository.save(user);
        return save;
    }

    @Override
    public List<User> getAll() {
        List<User> all = userRepository.findAll();
        return all;
    }

    @Override
    public User getUser(String id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User not found with id:"+id)
        );
        //GETTING RATING with user id
        Rating[] forObject = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{}",forObject);
        //converting array rating in list
        List<Rating> ratingList=Arrays.asList(forObject);

        //api call to hotel service
        List<Rating> collect = ratingList.stream().map(rating -> {
                    ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotel/"+rating.getHotelId(), Hotel.class);
                    Hotel body = forEntity.getBody();
                    rating.setHotel(body);
                    return rating;
                }
        ).collect(Collectors.toList());
        user.setRating(collect);
        return user;
    }
}

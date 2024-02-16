package com.user.service.controller;

import com.user.service.entity.User;
import com.user.service.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    Logger logger=LoggerFactory.getLogger(UserController.class);
     int retrycount=1;

    @GetMapping("/{userId}")
   // @CircuitBreaker(name="ratingHotelBreaker",fallbackMethod = "ratingFallbackMethod")
    //using retry method
  //  @Retry(name="ratingHotelService",fallbackMethod ="ratingFallbackMethod")
    //using rate limiter
    @RateLimiter(name="userRateLimiter",fallbackMethod = "ratingFallbackMethod")
    public ResponseEntity<User> getUser(@PathVariable("userId")String userId){
        logger.info("Retry count:{}",retrycount);
        retrycount++;
        User user = userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
    //creating circuit breaker fallback method
    public ResponseEntity<User> ratingFallbackMethod(String userId,Exception  ex){
       logger.info("Fallback method called because service is down: ",ex.getMessage());
        User user = User.builder().name("Dummy").email("Dummy@gmail.com").about("This user is created dummy because some service is down").userId("1234").build();
        return new ResponseEntity<>(user,HttpStatus.OK);

    }
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return new ResponseEntity<>(user1,HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> all = userService.getAll();
        return new ResponseEntity<>(all,HttpStatus.OK);
    }
}

package com.user.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="micro_users")
@Builder
public class User {
    @Id
 private String userId;  //not auto generating
    @Column(length = 25)
 private String name;
 private String email;
 private String about;

@Transient                 //it is used to not save in db
 private List<Rating> rating=new ArrayList<>();

}

package com.msmavas.speedy.models;


import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name="User") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    
    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

   
    
    

    @Column(name = "password")
    private String password;

   
    @Column
    private String otp;
    
  

    
    
    
    
    
    
    
}

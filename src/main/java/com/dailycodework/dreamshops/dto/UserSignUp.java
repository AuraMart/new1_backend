package com.dailycodework.dreamshops.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUp {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

package com.dailycodework.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content; 

    private LocalDateTime createdAt; 

    @Column(nullable = false)
    private int rating; 

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 
}

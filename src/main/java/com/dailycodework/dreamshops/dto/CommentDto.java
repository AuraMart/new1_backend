package com.dailycodework.dreamshops.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private int rating;
    private String productName;
    private String userName;
}

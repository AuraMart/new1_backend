package com.dailycodework.dreamshops.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotBlank(message = "Content cannot be empty")
    private String content;
    @NotNull(message = "Rating is required")
    private int rating;
    @NotNull(message = "Product ID is required")
    private Long productId;
    @NotNull(message = "User ID is required")
    private Long userId;
}

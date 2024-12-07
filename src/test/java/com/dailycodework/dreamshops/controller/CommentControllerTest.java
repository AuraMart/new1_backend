package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.CommentDto;
import com.dailycodework.dreamshops.request.CommentRequest;
import com.dailycodework.dreamshops.service.comment.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private CommentRequest commentRequest;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        commentRequest = new CommentRequest();
        commentRequest.setProductId(1L);
        commentRequest.setUserId(1L);
        commentRequest.setContent("Great product!");
        commentRequest.setRating(5);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setContent("Great product!");
        commentDto.setRating(5);
        commentDto.setProductName("Product A");
        commentDto.setUserName("John Doe");
    }

    @Test
    void testCreateComment() {
        // Arrange
        when(commentService.createComment(commentRequest)).thenReturn(commentDto);

        // Act
        ResponseEntity<CommentDto> response = commentController.createComment(commentRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Great product!", response.getBody().getContent());
        verify(commentService, times(1)).createComment(commentRequest);
    }

    @Test
    void testGetCommentsByProduct() {
        // Arrange
        List<CommentDto> comments = Collections.singletonList(commentDto);
        when(commentService.getCommentsByProduct(1L)).thenReturn(comments);

        // Act
        ResponseEntity<List<CommentDto>> response = commentController.getCommentsByProduct(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Great product!", response.getBody().get(0).getContent());
        verify(commentService, times(1)).getCommentsByProduct(1L);
    }
}


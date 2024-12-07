package com.dailycodework.dreamshops.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.CommentDto;
import com.dailycodework.dreamshops.request.CommentRequest;
import com.dailycodework.dreamshops.service.comment.CommentService;

@RestController
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<CommentDto> createComment(@Validated @RequestBody CommentRequest request) {
        CommentDto comment = commentService.createComment(request);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentDto>> getCommentsByProduct(@PathVariable Long productId) {
        List<CommentDto> comments = commentService.getCommentsByProduct(productId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }
}

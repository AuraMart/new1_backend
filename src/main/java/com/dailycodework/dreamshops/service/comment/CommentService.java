package com.dailycodework.dreamshops.service.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.dto.CommentDto;
import com.dailycodework.dreamshops.model.Comment;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CommentRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.request.CommentRequest;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CommentDto createComment(CommentRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setProduct(product);
        comment.setUser(user);
        comment.setCreatedAt(java.time.LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return convertToDto(savedComment);
    }

    public List<CommentDto> getCommentsByProduct(Long productId) {
        return commentRepository.findByProductId(productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setRating(comment.getRating());
        dto.setProductName(comment.getProduct().getName());
        dto.setUserName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
        return dto;
    }
}

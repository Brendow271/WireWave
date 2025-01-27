package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Comment;
import com.wirewave.wirewave.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByProductId(int productId) {
        return commentRepository.findByProductId(productId);
    }

    public List<Comment> getCommentsByUserId(Integer userId) {
        return commentRepository.findByUserId(userId);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    // Проверка существующего комментария
    public Optional<Comment> findByUserAndProduct(Integer userId, Integer productId) {
        return commentRepository.findByUserIdAndProductId(userId, productId);
    }

    // Фильтрация комментариев
    public List<Comment> findCommentsByRating(Integer minRating, Integer maxRating) {
        return commentRepository.findByEstimationBetween(minRating, maxRating);
    }
}

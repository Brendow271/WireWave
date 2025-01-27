package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Comment;
import com.wirewave.wirewave.service.CommentService;
import com.wirewave.wirewave.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductService productService;

    // Получить все комментарии
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // Получить комментарий по ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
        Comment comment = commentService.getCommentById(id);
        return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
    }

    // Получить комментарии по продукту
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Comment>> getCommentsByProductId(@PathVariable Integer productId) {
        List<Comment> comments = commentService.getCommentsByProductId(productId);
        return ResponseEntity.ok(comments);
    }

    // Создать новый комментарий или обновить существующий
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/addOrUpdate")
    public ResponseEntity<Comment> addOrUpdateComment(@RequestBody @Valid Comment comment) {
        Optional<Comment> existingComment = commentService.findByUserAndProduct(comment.getUser().getId(), comment.getProduct().getId());

        if (existingComment.isPresent()) {
            Comment existing = existingComment.get();
            existing.setEstimation(comment.getEstimation());
            existing.setDescription(comment.getDescription());
            existing.setPhoto(comment.getPhoto());
            commentService.saveComment(existing);
        } else {
            commentService.saveComment(comment);
        }

        productService.updateAverageRating(comment.getProduct().getId());
        return ResponseEntity.ok(comment);
    }

    // Фильтровать комментарии по диапазону оценок
    @GetMapping("/filter")
    public ResponseEntity<List<Comment>> filterCommentsByRating(
            @RequestParam(required = false, defaultValue = "1") Integer minRating,
            @RequestParam(required = false, defaultValue = "5") Integer maxRating) {
        List<Comment> comments = commentService.findCommentsByRating(minRating, maxRating);
        return ResponseEntity.ok(comments);
    }

    // Обновить существующий комментарий
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer id, @Valid @RequestBody Comment commentDetails) {
        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        comment.setEstimation(commentDetails.getEstimation());
        comment.setDescription(commentDetails.getDescription());
        comment.setPhoto(commentDetails.getPhoto());

        Comment updatedComment = commentService.saveComment(comment);
        productService.updateAverageRating(comment.getProduct().getId());
        return ResponseEntity.ok(updatedComment);
    }

    // Удалить комментарий
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        commentService.deleteComment(id);
        productService.updateAverageRating(comment.getProduct().getId());
        return ResponseEntity.noContent().build();
    }
}

package com.wirewave.wirewave.controller;

import com.wirewave.wirewave.entity.Comment;
import com.wirewave.wirewave.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

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

    // Получить комментарии по пользователю
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Integer userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    // Создать новый комментарий
    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) {
        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    // Обновить существующий комментарий
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
        return ResponseEntity.ok(updatedComment);
    }

    // Удалить комментарий
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        if (commentService.getCommentById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

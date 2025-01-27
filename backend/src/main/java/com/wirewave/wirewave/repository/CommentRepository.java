package com.wirewave.wirewave.repository;

import com.wirewave.wirewave.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Найти комментарии по ID продукта
    List<Comment> findByProductId(Integer productId);

    // Найти комментарии по ID пользователя
    List<Comment> findByUserId(Integer userId);

    // Проверка уникальности комментария для пользователя и продукта
    Optional<Comment> findByUserIdAndProductId(Integer userId, Integer productId);

    // Фильтрация комментариев по диапазону оценок
    @Query("SELECT c FROM Comment c WHERE c.estimation BETWEEN :minRating AND :maxRating")
    List<Comment> findByEstimationBetween(@Param("minRating") Integer minRating, @Param("maxRating") Integer maxRating);
}

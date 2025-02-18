package com.wirewave.wirewave.service;

import com.wirewave.wirewave.entity.Comment;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setRole(User.Role.USER);

        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");

        comment = new Comment();
        comment.setId(1);
        comment.setEstimation(5);
        comment.setDescription("Great product!");
        comment.setUser(user);
        comment.setProduct(product);
    }

    // Тест на получение всех комментариев
    @Test
    void shouldReturnAllComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> comments = commentService.getAllComments();

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    // Тест на получение комментария по ID
    @Test
    void shouldReturnCommentById() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getCommentById(1);

        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getId()).isEqualTo(comment.getId());
    }

    // Тест на получение комментария по ID, если его нет
    @Test
    void shouldReturnNullIfCommentNotExists() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        Comment foundComment = commentService.getCommentById(1);

        assertThat(foundComment).isNull();
    }

    // Тест на получение комментариев по продукту
    @Test
    void shouldReturnCommentsByProductId() {
        when(commentRepository.findByProductId(1)).thenReturn(List.of(comment));

        List<Comment> comments = commentService.getCommentsByProductId(1);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    // Тест на получение комментариев по пользователю
    @Test
    void shouldReturnCommentsByUserId() {
        when(commentRepository.findByUserId(1)).thenReturn(List.of(comment));

        List<Comment> comments = commentService.getCommentsByUserId(1);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    // Тест на сохранение комментария
    @Test
    void shouldSaveComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.saveComment(comment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isEqualTo(comment.getId());
    }

    // Тест на удаление комментария
    @Test
    void shouldDeleteComment() {
        doNothing().when(commentRepository).deleteById(1);

        commentService.deleteComment(1);

        verify(commentRepository, times(1)).deleteById(1);
    }

    // Тест на поиск комментария по пользователю и продукту
    @Test
    void shouldFindByUserAndProduct() {
        when(commentRepository.findByUserIdAndProductId(1, 1)).thenReturn(Optional.of(comment));

        Optional<Comment> foundComment = commentService.findByUserAndProduct(1, 1);

        assertThat(foundComment).isPresent();
        assertThat(foundComment.get()).isEqualTo(comment);
    }

    // Тест на фильтрацию комментариев по рейтингу
    @Test
    void shouldFilterCommentsByRating() {
        when(commentRepository.findByEstimationBetween(3, 5)).thenReturn(List.of(comment));

        List<Comment> comments = commentService.findCommentsByRating(3, 5);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }
}

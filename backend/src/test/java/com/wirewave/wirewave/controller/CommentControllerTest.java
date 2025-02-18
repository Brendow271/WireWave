package com.wirewave.wirewave.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirewave.wirewave.config.JwtAuthenticationFilter;
import com.wirewave.wirewave.config.JwtUtil;
import com.wirewave.wirewave.config.SecurityConfiguration;
import com.wirewave.wirewave.entity.Comment;
import com.wirewave.wirewave.entity.Product;
import com.wirewave.wirewave.entity.User;
import com.wirewave.wirewave.service.CommentService;
import com.wirewave.wirewave.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

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
    void shouldGetAllComments() throws Exception {
        Mockito.when(commentService.getAllComments()).thenReturn(List.of(comment));

        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description").value(comment.getDescription()));
    }

    // Тест на получение комментария по ID
    @Test
    void shouldGetCommentById() throws Exception {
        Mockito.when(commentService.getCommentById(1)).thenReturn(comment);

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.description").value(comment.getDescription()));
    }

    // Тест на получение комментария по ID, если его нет
    @Test
    void shouldReturnNotFoundIfCommentNotExists() throws Exception {
        Mockito.when(commentService.getCommentById(1)).thenReturn(null);

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isNotFound());
    }

    // Тест на добавление комментария
    @WithMockUser(roles = "USER")
    @Test
    void shouldAddComment() throws Exception {
        Mockito.when(commentService.findByUserAndProduct(user.getId(), product.getId())).thenReturn(Optional.empty());
        Mockito.when(commentService.saveComment(any(Comment.class))).thenReturn(comment);
        Mockito.doNothing().when(productService).updateAverageRating(product.getId());

        mockMvc.perform(post("/api/comments/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(comment.getDescription()));
    }

    // Тест на обновление комментария
    @WithMockUser(roles = "USER")
    @Test
    void shouldUpdateExistingComment() throws Exception {
        Comment updatedComment = new Comment();
        updatedComment.setId(1);
        updatedComment.setEstimation(4);
        updatedComment.setDescription("Updated review");
        updatedComment.setUser(user);
        updatedComment.setProduct(product);

        Mockito.when(commentService.findByUserAndProduct(user.getId(), product.getId())).thenReturn(Optional.of(comment));
        Mockito.when(commentService.saveComment(any(Comment.class))).thenReturn(updatedComment);
        Mockito.doNothing().when(productService).updateAverageRating(product.getId());

        mockMvc.perform(post("/api/comments/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated review"));
    }

    // Тест на фильтрацию комментариев по рейтингу
    @Test
    void shouldFilterCommentsByRating() throws Exception {
        Mockito.when(commentService.findCommentsByRating(3, 5)).thenReturn(List.of(comment));

        mockMvc.perform(get("/api/comments/filter")
                        .param("minRating", "3")
                        .param("maxRating", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].estimation").value(comment.getEstimation()));
    }

    // Тест на обновление комментария
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldUpdateComment() throws Exception {
        Comment updatedComment = new Comment();
        updatedComment.setId(1);
        updatedComment.setEstimation(4);
        updatedComment.setDescription("Updated description");

        Mockito.when(commentService.getCommentById(1)).thenReturn(comment);
        Mockito.when(commentService.saveComment(any(Comment.class))).thenReturn(updatedComment);
        Mockito.doNothing().when(productService).updateAverageRating(product.getId());

        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    // Тест на удаление комментария
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldDeleteComment() throws Exception {
        Mockito.when(commentService.getCommentById(1)).thenReturn(comment);
        Mockito.doNothing().when(commentService).deleteComment(1);
        Mockito.doNothing().when(productService).updateAverageRating(product.getId());

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNoContent());
    }

    // Тест на удаление несуществующего комментария
    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentComment() throws Exception {
        Mockito.when(commentService.getCommentById(1)).thenReturn(null);

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNotFound());
    }
}

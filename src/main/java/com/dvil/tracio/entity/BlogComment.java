package com.dvil.tracio.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "blog_comments")
@AllArgsConstructor
@NoArgsConstructor
public class BlogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_comment_id", nullable = false)
    private Integer id;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("comments")
    private User user;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonIgnoreProperties("comments")
    private Blog blog;
}

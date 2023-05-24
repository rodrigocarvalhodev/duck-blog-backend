package com.duckdeveloper.blog.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts", schema = "blog")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime createdTime;

    @SQLUpdate(sql = "SELECT CURRENT_TIMESTAMP;")
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
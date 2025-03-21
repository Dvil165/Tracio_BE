package com.dvil.tracio.controller;

import com.dvil.tracio.dto.BlogCommentDTO;
import com.dvil.tracio.service.BlogCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs/{blogId}/comments")
public class BlogCommentController {
    private final BlogCommentService commentService;

    public BlogCommentController(BlogCommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<BlogCommentDTO>> getComments(@PathVariable Integer blogId) {
        return ResponseEntity.ok(commentService.getCommentsByBlogId(blogId));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BlogCommentDTO> addComment(@PathVariable Integer blogId, @RequestBody BlogCommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.createComment(blogId, commentDTO));
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BlogCommentDTO> updateComment(@PathVariable Integer blogId, @PathVariable Integer commentId, @RequestBody BlogCommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateComment(blogId, commentId, commentDTO));
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(@PathVariable Integer blogId, @PathVariable Integer commentId) {
        commentService.deleteComment(blogId, commentId);
        return ResponseEntity.ok().body(Map.of(
                "message", "Xoá comment thành công!",
                "commentId", commentId
        ));
    }

}

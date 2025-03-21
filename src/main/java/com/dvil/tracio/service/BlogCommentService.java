package com.dvil.tracio.service;

import com.dvil.tracio.dto.BlogCommentDTO;
import java.util.List;

public interface BlogCommentService {
    List<BlogCommentDTO> getCommentsByBlogId(Integer blogId);
    BlogCommentDTO createComment(Integer blogId, BlogCommentDTO commentDTO);
    BlogCommentDTO updateComment(Integer blogId, Integer commentId, BlogCommentDTO commentDTO);
    void deleteComment(Integer blogId, Integer commentId);
}

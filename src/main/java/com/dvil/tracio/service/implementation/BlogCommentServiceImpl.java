package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.BlogCommentDTO;
import com.dvil.tracio.entity.Blog;
import com.dvil.tracio.entity.BlogComment;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.BlogCommentMapper;
import com.dvil.tracio.repository.BlogCommentRepo;
import com.dvil.tracio.repository.BlogRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.BlogCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {
    private final BlogCommentRepo commentRepo;
    private final BlogRepo blogRepo;
    private final UserRepo userRepo;
    private final BlogCommentMapper commentMapper = BlogCommentMapper.INSTANCE;

    public BlogCommentServiceImpl(BlogCommentRepo commentRepo, BlogRepo blogRepo, UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.blogRepo = blogRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<BlogCommentDTO> getCommentsByBlogId(Integer blogId) {
        List<BlogCommentDTO> comments = commentRepo.findByBlogId(blogId)
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());

        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có comment nào cho blog này.");
        }

        return comments;
    }

    @Override
    @Transactional
    public BlogCommentDTO createComment(Integer blogId, BlogCommentDTO commentDTO) {
        User user = getCurrentUser();

        Blog blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog không tồn tại"));

        BlogComment comment = new BlogComment();
        comment.setText(commentDTO.getText());
        comment.setCreatedAt(OffsetDateTime.now());
        comment.setBlog(blog);
        comment.setUser(user);

        comment = commentRepo.save(comment);
        return commentMapper.toDTO(comment);
    }

    @Override
    @Transactional
    public BlogCommentDTO updateComment(Integer blogId, Integer commentId, BlogCommentDTO commentDTO) {
        User user = getCurrentUser();
        BlogComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy comment với ID " + commentId));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền chỉnh sửa. ");
        }

        comment.setText(commentDTO.getText());

        return commentMapper.toDTO(commentRepo.save(comment));
    }


    @Override
    @Transactional
    public void deleteComment(Integer blogId, Integer commentId) {
        User user = getCurrentUser();
        Blog blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy blog với ID " + blogId));

        BlogComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy comment với ID " + commentId));

        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isBlogOwner = blog.getCreatedBy().getId().equals(user.getId());
        boolean isCommentOwner = comment.getUser().getId().equals(user.getId());


        if (!isAdmin && !isBlogOwner && !isCommentOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xoá comment này.");
        }

        commentRepo.delete(comment);
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepo.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng"));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không thể xác thực người dùng");
    }
}

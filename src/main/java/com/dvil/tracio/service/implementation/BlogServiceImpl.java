package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.BlogDTO;
import com.dvil.tracio.entity.Blog;
import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.mapper.BlogMapper;
import com.dvil.tracio.repository.BlogRepo;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.BlogService;
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
public class BlogServiceImpl implements BlogService {
    private final BlogRepo blogRepo;
    private final UserRepo userRepo;
    private final BlogMapper blogMapper = BlogMapper.INSTANCE;

    public BlogServiceImpl(BlogRepo blogRepo, UserRepo userRepo) {
        this.blogRepo = blogRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
        List<BlogDTO> blogs = blogRepo.findAll().stream()
                .map(blogMapper::toDTO)
                .collect(Collectors.toList());

        if (blogs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có blog nào được tạo.");
        }

        return blogs;
    }

    @Override
    public BlogDTO getBlogById(Integer id) {
        Blog blog = blogRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy blog với ID " + id));
        return blogMapper.toDTO(blog);
    }

    @Override
    @Transactional
    public BlogDTO createBlog(BlogDTO blogDTO) {
        User user = getCurrentUser();

        boolean isAdminOrCyclist = user.getRole().equals(RoleName.ADMIN) || user.getRole().equals(RoleName.CYCLIST);
        if (!isAdminOrCyclist) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền tạo blog.");
        }

        Blog blog = blogMapper.toEntity(blogDTO);
        blog.setCreatedBy(user);
        blog.setCreatedAt(blog.getCreatedAt() != null ? blog.getCreatedAt() : OffsetDateTime.now());

        blog = blogRepo.save(blog);
        return blogMapper.toDTO(blog);
    }

    @Override
    @Transactional
    public BlogDTO updateBlog(Integer id, BlogDTO blogDTO) {
        User user = getCurrentUser();
        Blog existingBlog = blogRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy blog với ID " + id));

        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isOwner = existingBlog.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật blog này.");
        }

        existingBlog.setTitle(blogDTO.getTitle());
        existingBlog.setContent(blogDTO.getContent());
        existingBlog.setImageUrl(blogDTO.getImageUrl());

        return blogMapper.toDTO(blogRepo.save(existingBlog));
    }

    @Override
    @Transactional
    public void deleteBlog(Integer id) {
        User user = getCurrentUser();
        Blog blog = blogRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy blog với ID " + id));

        boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
        boolean isOwner = blog.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xoá blog này.");
        }

        blogRepo.delete(blog);
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

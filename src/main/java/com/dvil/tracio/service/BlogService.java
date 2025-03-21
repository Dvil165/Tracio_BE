package com.dvil.tracio.service;

import com.dvil.tracio.dto.BlogDTO;
import java.util.List;

public interface BlogService {
    List<BlogDTO> getAllBlogs();
    BlogDTO getBlogById(Integer id);
    BlogDTO createBlog(BlogDTO blogDTO);
    BlogDTO updateBlog(Integer id, BlogDTO blogDTO);
    void deleteBlog(Integer id);
}

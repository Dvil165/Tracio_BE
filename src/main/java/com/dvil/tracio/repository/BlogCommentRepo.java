package com.dvil.tracio.repository;

import com.dvil.tracio.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BlogCommentRepo extends JpaRepository<BlogComment, Integer> {
    List<BlogComment> findByBlogId(Integer blogId);
}

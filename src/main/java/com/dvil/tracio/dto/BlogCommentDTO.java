package com.dvil.tracio.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
public class BlogCommentDTO {
    private Integer id;
    private String text;
    private OffsetDateTime createdAt;
    private Integer blogId;
    private Integer userId;
}

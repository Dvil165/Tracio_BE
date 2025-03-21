package com.dvil.tracio.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
public class BlogDTO {
    private Integer id;
    private String title;
    private String content;
    private OffsetDateTime createdAt;
    private String imageUrl;
    private Integer createdByUserId;
    private String createdByUsername;
}

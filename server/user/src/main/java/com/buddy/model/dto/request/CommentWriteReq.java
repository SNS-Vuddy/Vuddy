package com.buddy.model.dto.request;

import lombok.Data;

@Data
public class CommentWriteReq {

    private Long feedId;
    private String content;
}

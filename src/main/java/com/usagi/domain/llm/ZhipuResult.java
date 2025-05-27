package com.usagi.domain.llm;

import lombok.Data;

@Data
public class ZhipuResult {
    private int code;
    private String msg;
    private boolean success;
    private EmbeddingResult data;
}

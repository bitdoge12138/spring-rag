package com.usagi.controller;

import com.usagi.component.TxtChunk;
import com.usagi.component.VectorStorage;
import com.usagi.domain.llm.ChunkResult;
import com.usagi.domain.llm.EmbeddingResult;
import com.usagi.entity.ApiResponse;
import com.usagi.llm.ZhipuAI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddTxtController {

    private static final Logger log = LoggerFactory.getLogger(AddTxtController.class);

    private final TxtChunk txtChunk;
    private final VectorStorage vectorStorage;
    private final ZhipuAI zhipuAI;

    public AddTxtController(TxtChunk txtChunk, VectorStorage vectorStorage, ZhipuAI zhipuAI) {
        this.txtChunk = txtChunk;
        this.vectorStorage = vectorStorage;
        this.zhipuAI = zhipuAI;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addDocument(@RequestParam("file") MultipartFile file) {
        log.info("start add doc");

        try {
            // 加载文档并分块
            List<ChunkResult> chunkResults = txtChunk.chunk(file);

            String doc = file.getOriginalFilename();

            // 向量化处理
            List<EmbeddingResult> embeddingResults = zhipuAI.embedding(chunkResults);

            // 存储向量
            String collection = vectorStorage.getCollectionName();
            vectorStorage.store(collection, embeddingResults, doc);

            log.info("finished processing doc: {}", doc);

            // 返回成功响应
            return ResponseEntity.ok(
                    ApiResponse.success("Document processed successfully", doc)
            );
        } catch (Exception e) {
            log.error("Error adding document: {}", e.getMessage(), e);

            // 返回错误响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }
}    
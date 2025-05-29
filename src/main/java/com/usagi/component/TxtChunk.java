package com.usagi.component;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.usagi.domain.llm.ChunkResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@AllArgsConstructor
public class TxtChunk {

    public List<ChunkResult> chunk(MultipartFile file) {
        log.info("start chunk---> docId");
        try {
            String originalFilename = file.getOriginalFilename();
            String docId = extractDocId(originalFilename);
            log.info("extracted docId: {}", docId);
            // 读取上传的文件内容
            String txt = IoUtil.read(file.getInputStream(), StandardCharsets.UTF_8);

            // 按固定字数分割，256
            String[] lines = StrUtil.split(txt, 256);
            log.info("chunk size:{}", ArrayUtil.length(lines));

            List<ChunkResult> results = new ArrayList<>();
            AtomicInteger atomicInteger = new AtomicInteger(0);

            for (String line : lines) {
                ChunkResult chunkResult = new ChunkResult();
                chunkResult.setDocId(docId);
                chunkResult.setContent(line);
                chunkResult.setChunkId(atomicInteger.incrementAndGet());
                results.add(chunkResult);
            }

            return results;
        } catch (IOException e) {
            log.error("处理上传文件失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 从文件名中提取docId（去除扩展名）
    @NotNull
    private String extractDocId(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "unknown";
        }
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex == -1) {
            return filename;
        }
        return filename.substring(0, lastIndex);
    }
}

package com.usagi.command;

import com.usagi.component.TxtChunk;
import com.usagi.component.VectorStorage;

import com.usagi.domain.llm.ChunkResult;
import com.usagi.domain.llm.EmbeddingResult;
import com.usagi.llm.ZhipuAI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@Slf4j
@AllArgsConstructor
@ShellComponent
public class AddTxtCommand {

    final TxtChunk txtChunk;
    final VectorStorage vectorStorage;
    final ZhipuAI zhipuAI;

    @ShellMethod(value = "add local txt data")
    public String add(String doc){
        log.info("start add doc.");
        // 加载
        List<ChunkResult> chunkResults= txtChunk.chunk(doc);
        // embedding
        List<EmbeddingResult> embeddingResults=zhipuAI.embedding(chunkResults);
        // store vector
        String collection= vectorStorage.getCollectionName();
        vectorStorage.store(collection,embeddingResults,doc);
        log.info("finished");
        return "finished docId:{}"+doc;
    }
}

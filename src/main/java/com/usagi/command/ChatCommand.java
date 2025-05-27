package com.usagi.command;

import cn.hutool.core.util.StrUtil;
import com.usagi.component.VectorStorage;
import com.usagi.llm.ZhipuAI;
import com.usagi.utils.LLMUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@AllArgsConstructor
@Slf4j
@ShellComponent
public class ChatCommand {

    final VectorStorage vectorStorage;
    final ZhipuAI zhipuAI;

    @ShellMethod(value = "chat with files")
    public String chat(String question){
        if (StrUtil.isBlank(question)){
            return "You must send a question";
        }
        //句子转向量
        double[] vector=zhipuAI.sentence(question);
        // 向量召回
        String collection= vectorStorage.getCollectionName();
        String vectorData=vectorStorage.retrieval(collection,vector);
        if (StrUtil.isBlank(vectorData)){
            return "No Answer!";
        }
        // 构建Prompt
        String prompt= LLMUtils.buildPrompt(question,vectorData);
        zhipuAI.chat(prompt);
        // 大模型对话
        //return "you question:{}"+question+"finished.";
        return StrUtil.EMPTY;
    }

}

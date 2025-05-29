package com.usagi.controller;

import cn.hutool.core.util.StrUtil;
import com.usagi.component.VectorStorage;
import com.usagi.llm.ZhipuAI;
import com.usagi.utils.LLMUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final VectorStorage vectorStorage;
    private final ZhipuAI zhipuAI;

    @GetMapping("/chat")
    public String chatPage() {
        return "chat.html"; // 显示chat.html页面
    }

    @PostMapping("/chat")
    public String chat(@RequestParam("question") String question, Model model) {
        if (StrUtil.isBlank(question)) {
            model.addAttribute("answer", "You must send a question");
            return "chat.html";
        }

        double[] vector = zhipuAI.sentence(question);
        String collection = vectorStorage.getCollectionName();
        String vectorData = vectorStorage.retrieval(collection, vector);

        if (StrUtil.isBlank(vectorData)) {
            model.addAttribute("answer", "No Answer!");
            return "chat.html";
        }

        String prompt = LLMUtils.buildPrompt(question, vectorData);
        String answer = zhipuAI.chat(prompt); // 改为返回结果
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);

        return "chat.html";
    }
}

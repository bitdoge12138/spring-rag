package com.usagi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "llm")
public class LLmProperties {
    /**
     * 智谱AI的开发密钥，https://open.bigmodel.cn/dev/api#text_embedding
     * 注册智谱AI开放平台获取
     */
    private String zpKey;
}

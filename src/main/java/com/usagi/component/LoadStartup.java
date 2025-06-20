package com.usagi.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class LoadStartup implements InitializingBean {

    final VectorStorage vectorStorage;


    public void startup(){
        log.info("init vector collection");
        String collectionName= vectorStorage.getCollectionName();
        log.info("init collection:{}",collectionName);
        //向量维度固定1024，根据选择的向量Embedding模型的维度确定最终维度
        // 这里因为选择智谱的Embedding模型，维度是1024，所以固定为该值
        vectorStorage.initCollection(collectionName,1024);
        log.info("init collection success.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("start load.....");
        this.startup();
    }
}

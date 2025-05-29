package com.usagi.listener;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 描述： sse event listener
 */
@AllArgsConstructor
@Slf4j
public class ConsoleEventSourceListener extends EventSourceListener {
    final CountDownLatch countDownLatch;
    
    @Override
    public void onOpen(EventSource eventSource, Response response) {
        //log.info("LLM建立sse连接...");
    }
    
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
       // log.info("LLM返回,id:{},type:{},数据：{}", id, type, data);
        System.out.print(data);
        if (data.contains("\n")){
            System.out.println("换行了");
        }
        if (StrUtil.equalsIgnoreCase(type,"finish")){
            //end
            System.out.println(StrUtil.EMPTY);
        }
    }
    
    @Override
    public void onClosed(EventSource eventSource) {
       // log.info("LLM关闭sse连接...");
        countDownLatch.countDown();
    }
    
    @SneakyThrows
    @Override
    public void onFailure( EventSource eventSource, Throwable t, Response response) {
        countDownLatch.countDown();
        if (Objects.isNull(response)) {
            log.error("LLM  sse连接异常", t);
            eventSource.cancel();
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("error1-----------------");
            log.error("LLM  sse连接异常data：{}", body.string(), t);
        } else {
            log.error("error2-----------------");
            log.error("LLM  sse连接异常data：{}", response, t);
        }
        eventSource.cancel();
    }
}

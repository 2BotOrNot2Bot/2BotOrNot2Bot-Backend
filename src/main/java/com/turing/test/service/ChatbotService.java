package com.turing.test.service;

import com.turing.test.domain.Chatbot;
import com.turing.test.vo.ResultVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 15:26
**/
@Service
public interface ChatbotService {

    @Async("threadPoolTaskExecutor")
    CompletableFuture<ResultVo<Double>> updateChatbotStat(String name, Boolean result) throws ExecutionException, InterruptedException;

    ResultVo<List<Chatbot>> getSortedChatbotStat() throws ExecutionException, InterruptedException;

    ResultVo<String> clearChatbotStat(String name) throws ExecutionException, InterruptedException;

    ResultVo<Integer> resetChatbots() throws ExecutionException, InterruptedException;
}

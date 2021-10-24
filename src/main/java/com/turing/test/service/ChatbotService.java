package com.turing.test.service;

import com.turing.test.vo.ResultVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 15:26
**/
@Service
public interface ChatbotService {

    @Async
    ResultVo<Double> updateChatbotStat(String name, Boolean result) throws ExecutionException, InterruptedException;

}

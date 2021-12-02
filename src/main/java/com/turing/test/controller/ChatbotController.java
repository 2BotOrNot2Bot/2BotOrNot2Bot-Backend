package com.turing.test.controller;

import com.turing.test.domain.Chatbot;
import com.turing.test.service.ChatbotService;
import com.turing.test.service.UserService;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 16:10
**/
@RestController
public class ChatbotController {

    @Autowired
    ChatbotService chatbotService;

    @Autowired
    UserService userService;

    @PostMapping("/chatbots/stats")
    public ResultVo<Double> updateChatbotStat(@RequestBody Map<String,String> chatbotMap) throws InterruptedException, ExecutionException {
        if(!chatbotMap.containsKey("name") || !chatbotMap.containsKey("result") || !chatbotMap.containsKey("uid"))
            return ResultVo.error(BusinessError.INVALID_PARAM);
        CompletableFuture<ResultVo<Double>> futureResult = chatbotService.updateChatbotStat
                (chatbotMap.get("name"),!Boolean.parseBoolean(chatbotMap.get("result")));
        userService.updateUserPoints(chatbotMap.get("uid"),Boolean.parseBoolean(chatbotMap.get("result")));
        return futureResult.get();
    }

    @GetMapping("/chatbots/stats")
    public ResultVo<List<Chatbot>> getChatbotStat() throws InterruptedException, ExecutionException {
        return chatbotService.getSortedChatbotStat();
    }

    @DeleteMapping("/chatbots/stats")
    public ResultVo<String> clearChatbotStat(@RequestBody String name) throws ExecutionException, InterruptedException {
        return chatbotService.clearChatbotStat(name);
    }

}
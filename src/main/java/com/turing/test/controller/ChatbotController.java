package com.turing.test.controller;

import com.turing.test.domain.User;
import com.turing.test.service.ChatbotService;
import com.turing.test.service.UserService;
import com.turing.test.service.dto.UserDto;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/chatbots")
    public ResultVo<Double> updateChatbotStat(@RequestBody Map<String,String> chatbotMap) throws InterruptedException, ExecutionException {
        if(!chatbotMap.containsKey("name") || !chatbotMap.containsKey("result"))
            return ResultVo.error(BusinessError.INVALID_PARAM);
        CompletableFuture<ResultVo<Double>> futureResult = chatbotService.updateChatbotStat
                (chatbotMap.get("name"),Boolean.valueOf(chatbotMap.get("result")));
        return futureResult.get();
    }

}
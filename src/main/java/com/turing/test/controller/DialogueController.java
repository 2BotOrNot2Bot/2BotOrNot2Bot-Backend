package com.turing.test.controller;

import com.turing.test.domain.form.UserIdForm;
import com.turing.test.service.DialogueService;
import com.turing.test.service.util.RedisUtils;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
* @Author Yibo Wen
* @Date 10/28/2021 3:28 PM
**/
@RestController
public class DialogueController {

    @Autowired
    DialogueService dialogueService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private Environment env;

    @PostMapping("/dialogues/search")
    public ResultVo<String> startSearch(@RequestBody UserIdForm form) {
        return dialogueService.startSearch(form.getUid());
    }

    @GetMapping("/dialogues/search")
    public ResultVo<Pair<String,String>> getOpponent(@RequestParam String uid) {
        return dialogueService.findOpponent(uid);
    }

    @PostMapping("/dialogues")
    public ResultVo<Long> startDialogue(@RequestParam String chatbot) {
        return dialogueService.startDialogue(chatbot);
    }

    @PatchMapping("/dialogues")
    public ResultVo<String> continueDialogue(@RequestBody Map<String,String> dialogueMap) throws IOException {
        if(!dialogueMap.containsKey("input") || !dialogueMap.containsKey("chatbot") || !dialogueMap.containsKey("uid"))
            return ResultVo.error(BusinessError.INVALID_PARAM);
        return dialogueService.getResponse(dialogueMap.get("input"),dialogueMap.get("chatbot"),dialogueMap.get("uid"));
    }

    @DeleteMapping("/dialogues")
    public ResultVo<Boolean> endDialogue(@RequestParam String uid) {
        return dialogueService.endDialogue(uid);
    }

    @DeleteMapping("/cache")
    public ResultVo<Boolean> clearCache(@RequestHeader("Authorization") String auth) {
        if(auth.contains(env.getProperty("AUTH_CODE"))){
            return ResultVo.success(redisUtils.clearAll());
        }
        return ResultVo.error(BusinessError.ACCESS_NOT_GRANTED);
    }

}

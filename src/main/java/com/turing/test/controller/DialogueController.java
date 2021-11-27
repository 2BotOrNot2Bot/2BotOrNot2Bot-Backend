package com.turing.test.controller;

import com.turing.test.service.DialogueService;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("dialogues/search")
    public ResultVo<Boolean> startSearch(String uid) {
        return dialogueService.startSearch(uid);
    }

    @GetMapping("dialogues/search")
    public ResultVo<String> getOpponent(String uid) {
        return dialogueService.findOpponent(uid);
    }

    @PostMapping("dialogues")
    public ResultVo<Long> startDialogue(String chatbot) throws IOException {
        return dialogueService.startDialogue(chatbot);
    }

    @PatchMapping("dialogues")
    public ResultVo<String> continueDialogue(@RequestBody Map<String,String> dialogueMap) throws IOException {
        if(!dialogueMap.containsKey("input") || !dialogueMap.containsKey("chatbot") || !dialogueMap.containsKey("session"))
            return ResultVo.error(BusinessError.INVALID_PARAM);
        return dialogueService.getResponse(dialogueMap.get("input"),dialogueMap.get("chatbot"),dialogueMap.get("session"));
    }

    @DeleteMapping("dialogues")
    public ResultVo<Boolean> endDialogue(String uid) {
        return dialogueService.endDialogue(uid);
    }

}

package com.turing.test.controller;

import com.turing.test.service.DialogueService;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("dialogues")
    public ResultVo<Long> startDialogue(String chatbot) throws IOException {
        return dialogueService.startDialogue(chatbot);
    }

    @PatchMapping("dialogues")
    public ResultVo<String> continueDialogue(@RequestBody Map<String,String> dialogueMap) throws IOException {
        if(!dialogueMap.containsKey("input") || !dialogueMap.containsKey("chatbot"))
            return ResultVo.error(BusinessError.INVALID_PARAM);
        return dialogueService.getResponse(dialogueMap.get("input"),dialogueMap.get("chatbot"));
    }
}

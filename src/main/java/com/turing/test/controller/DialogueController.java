package com.turing.test.controller;

import com.turing.test.service.DialogueService;
import com.turing.test.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
}

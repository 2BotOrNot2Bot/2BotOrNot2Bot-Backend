package com.turing.test.service;

import com.turing.test.vo.ResultVo;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
* @Author Yibo Wen
* @Date 10/28/2021 3:41 PM
**/
@Service
public interface DialogueService {

    ResultVo<Boolean> startSearch(String uid);

    ResultVo<String> findOpponent(String uid);

    ResultVo<Long> startDialogue(String chatbot);

    ResultVo<String> getResponse(String input, String chatbot, String sessionId);

    ResultVo<Boolean> endDialogue(String uid);
}

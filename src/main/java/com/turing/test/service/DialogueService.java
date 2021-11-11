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

    ResultVo<Long> startDialogue(String chatbot) throws IOException;

    ResultVo<String> getResponse(String input, String chatbot) throws IOException;
}

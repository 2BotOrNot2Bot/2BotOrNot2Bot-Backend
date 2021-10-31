package com.turing.test.service.impl;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.turing.test.domain.enums.Chatbots;
import com.turing.test.service.DialogueService;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
* @Author Yibo Wen
* @Date 10/28/2021 3:43 PM
**/
@Slf4j
@Service
public class DialogueServiceImpl implements DialogueService {
    @Override
    public ResultVo<Long> startDialogue(String chatbot) throws IOException {
        // DialogFlow API
        if(Chatbots.DIALOGFLOW.name().equals(chatbot)){
            try (SessionsClient sessionsClient = SessionsClient.create()) {
                SessionName session = SessionName.ofProjectSessionName("[PROJECT]", "[SESSION]");
                QueryInput queryInput = QueryInput.newBuilder().build();
                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            }
        }
        // PANDORABOTS API TODO
        else if(Chatbots.PANDORA_BOTS.name().equals(chatbot)){

        }
        // RASA API TODO
        else if(Chatbots.RASA.name().equals(chatbot)){

        }
        return null;
    }
}

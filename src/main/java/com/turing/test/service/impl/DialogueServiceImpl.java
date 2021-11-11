package com.turing.test.service.impl;

import com.google.cloud.dialogflow.v2.*;
import com.turing.test.service.DialogueService;
import com.turing.test.vo.BusinessError;
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
        if(chatbot.equals("dialogflow")){
            try (SessionsClient sessionsClient = SessionsClient.create()) {
                SessionName session = SessionName.ofProjectSessionName("[PROJECT]", "[SESSION]");
                QueryInput queryInput = QueryInput.newBuilder().build();
                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            }
        }
        return null;
    }

    @Override
    public ResultVo<String> getResponse(String input, String chatbot) throws IOException {
        // DialogFlow API
        if(chatbot.equals("dialogflow")){
            try (SessionsClient sessionsClient = SessionsClient.create()) {
                SessionName session = SessionName.ofProjectSessionName("[PROJECT]", "[SESSION]");
                // Set the text (hello) and language code (en-US) for the query
                TextInput.Builder textInput =
                        TextInput.newBuilder().setText(input).setLanguageCode("en");
                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
                QueryResult queryResult = response.getQueryResult();
                if(queryResult.getFulfillmentMessagesCount() == 0){
                    // default fallback intent triggered
                    return ResultVo.error(BusinessError.FALLBACK_INTENT);
                }
                return ResultVo.success(queryResult.getFulfillmentMessages(0).getText().getText(0));
            }
        }
        log.warn("DialogueServiceImpl->getResponse: no such chatbot found");
        return ResultVo.error(BusinessError.INVALID_PARAM);
    }
}

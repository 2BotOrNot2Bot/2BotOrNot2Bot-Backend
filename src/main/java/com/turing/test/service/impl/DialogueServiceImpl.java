package com.turing.test.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import com.turing.test.domain.enums.Chatbots;
import com.google.cloud.dialogflow.v2.*;
import com.turing.test.service.DialogueService;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;

/**
* @Author Yibo Wen
* @Date 10/28/2021 3:43 PM
**/
@Slf4j
@Service
public class DialogueServiceImpl implements DialogueService {

    @Autowired
    private Environment env;

    @Override
    public ResultVo<Long> startDialogue(String chatbot) throws IOException {
        // DialogFlow API
        if(Chatbots.DIALOGFLOW.getName().equals(chatbot)){
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

    @Override
    public ResultVo<String> getResponse(String input, String chatbot, String sessionId) throws IOException {
        // DialogFlow API
        if(Chatbots.DIALOGFLOW.getName().equals(chatbot)){
            // Instantiates a client
            try (SessionsClient sessionsClient = SessionsClient.create(authDialogflow())) {
                SessionName session = SessionName.ofProjectSessionName(env.getProperty("DIALOGFLOW_PROJECT_ID"), sessionId);
                // Set the text (hello) and language code (en-US) for the query
                TextInput.Builder textInput =
                        TextInput.newBuilder().setText(input).setLanguageCode("en");
                // Build the query with the TextInput
                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
                // Performs the detect intent request
                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
                // Display the query result
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

    private SessionsSettings authDialogflow() throws IOException {
        InputStream serviceAccount =
                this.getClass().getClassLoader().getResourceAsStream("dialogflowServiceAccountKey.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        String projectId = ((ServiceAccountCredentials)credentials).getProjectId();
        SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
        return settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
    }
}

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
import com.turing.test.service.util.RedisKey;
import com.turing.test.service.util.RedisLock;
import com.turing.test.service.util.RedisUtils;
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

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ResultVo<Boolean> startSearch(String uid) {
        String key = RedisKey.WAITING_QUEUE.getKey();
        // Need to modify waiting queue, obtain lock
        redisLock.tryLock(key,uid);
        // Add to waiting queue in Redis
        redisUtils.addToSet(key,uid);
        // Release lock
        redisLock.tryUnlock(key, uid);
        log.info("DialogueServiceImpl->startSearch: Added {} to queue",uid);
        return ResultVo.success(Boolean.TRUE);
    }

    @Override
    public ResultVo<String> findOpponent(String uid) {
        String key = RedisKey.WAITING_QUEUE.getKey();
        // Need to modify waiting queue, obtain lock
        redisLock.tryLock(key,uid);
        String opponent = redisUtils.popTwo(key,uid);
        if(opponent==null){
            log.info("DialogueServiceImpl->findOpponent: Not enough people waiting");
            // Release lock
            redisLock.tryUnlock(key, uid);
            return ResultVo.success(null);
        }
        log.info("DialogueServiceImpl->findOpponent: Found opponent {}",opponent);
        // Release lock
        redisLock.tryUnlock(key, uid);
        return ResultVo.success(opponent);
    }

    @Override
    public ResultVo<Long> startDialogue(String chatbot) throws IOException {
//        // DialogFlow API
//        if(Chatbots.DIALOGFLOW.getName().equals(chatbot)){
//            try (SessionsClient sessionsClient = SessionsClient.create()) {
//                SessionName session = SessionName.ofProjectSessionName("[PROJECT]", "[SESSION]");
//                QueryInput queryInput = QueryInput.newBuilder().build();
//                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
//            }
//        }
//        // PANDORABOTS API TODO
//        else if(Chatbots.PANDORA_BOTS.name().equals(chatbot)){
//
//        }
//        // RASA API TODO
//        else if(Chatbots.RASA.name().equals(chatbot)){
//
//        }
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

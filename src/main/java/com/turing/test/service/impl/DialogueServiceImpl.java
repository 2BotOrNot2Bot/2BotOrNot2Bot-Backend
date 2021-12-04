package com.turing.test.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.Tuple;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turing.test.domain.enums.Chatbots;
import com.google.cloud.dialogflow.v2.*;
import com.turing.test.service.DialogueService;
import com.turing.test.service.util.RedisKey;
import com.turing.test.service.util.RedisUtils;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
* @Author Yibo Wen, Yuxing Zhou
* @Date 10/28/2021 3:43 PM
**/
@Slf4j
@Service
public class DialogueServiceImpl implements DialogueService {

    @Autowired
    private Environment env;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ResultVo<String> startSearch(String uid) {
        if(uid==null||uid.isEmpty()) {
            uid= UUID.randomUUID().toString();
            log.info("DialogueServiceImpl->startSearch: Guest user {} started search",uid);
        }
        String key = RedisKey.WAITING_QUEUE.getKey();
        // Add to waiting queue in Redis, lock acquired and released
        Boolean result = redisUtils.addToSet(key,uid);
        log.info("DialogueServiceImpl->startSearch: Added {} to queue",uid);
        return result ? ResultVo.success(uid) : ResultVo.error(BusinessError.UNKNOWN_ERROR);
    }

    @Override
    public ResultVo<Pair<String,String>> findOpponent(String uid) {
        String waiting_key = RedisKey.WAITING_QUEUE.getKey();
        String chatting_key = RedisKey.CHATTING_STATUS.getKey();
        // Still in Waiting Queue
        if(redisUtils.findInSet(waiting_key,uid)){
            // Need to modify waiting queue, lock acquired and released
            String opponent = redisUtils.popTwo(waiting_key,uid);
            if(opponent==null){
                log.info("DialogueServiceImpl->findOpponent: Not enough people waiting");
                return ResultVo.success(null);
            }
            log.info("DialogueServiceImpl->findOpponent: Found opponent {}",opponent);
            // Need to change both players chatting status, obtain lock
            redisUtils.updateToHash(chatting_key,uid,opponent);
            redisUtils.updateToHash(chatting_key,opponent,uid);
            log.info("DialogueServiceImpl->findOpponent: Updated chatting status");
            return ResultVo.success(Pair.of(opponent,Chatbots.getRandomName()));
        }
        // Already removed from Waiting Queue by an opponent
        else {
            String opponent = redisUtils.getValueInHash(chatting_key,uid);
            if(opponent==null){
                log.error("DialogueServiceImpl->findOpponent: Redis Hash does not contain this uid");
                return ResultVo.error(BusinessError.UNKNOWN_ERROR);
            }
            log.info("DialogueServiceImpl->findOpponent: Found opponent {}",opponent);
            return ResultVo.success(Pair.of(opponent,Chatbots.getRandomName()));
        }
    }

    @Override
    public ResultVo<Long> startDialogue(String chatbot) {
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
    public ResultVo<String> getResponse(String input, String chatbot, String sessionId) {
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
            } catch (IOException e) {
                log.info("DialogueServiceImpl->getResponse: IOException");
                e.printStackTrace();
            }
        } else if(Chatbots.HARLEY.getName().equals(chatbot)){
            // Haven't implemented yet
        } else if (Chatbots.BRAINSHOP.getName().equals(chatbot)){
            try {
                HttpResponse<String> response = Unirest.get("https://acobot-brainshop-ai-v1.p.rapidapi.com/get?bid=178&key=sX5A2PcYZbsN5EY6&uid=mashape&msg="+input)
                        .header("x-rapidapi-host", "acobot-brainshop-ai-v1.p.rapidapi.com")
                        .header("x-rapidapi-key", "9d4fe34aadmsha68f858be4546dfp1c1075jsn843af4fa523e")
                        .asString();
//                log.info(JsonParser.parseString(response.getBody()).getAsJsonObject().toString());
                return ResultVo.success(JsonParser.parseString(response.getBody()).getAsJsonObject().get("cnt").toString());
            } catch (UnirestException e) {
                e.printStackTrace();
                return ResultVo.error(BusinessError.INVALID_PARAM);
            }
        }
        log.warn("DialogueServiceImpl->getResponse: no such chatbot found");
        return ResultVo.error(BusinessError.INVALID_PARAM);
    }

    @Override
    public ResultVo<Boolean> endDialogue(String uid) {
        String key = RedisKey.CHATTING_STATUS.getKey();
        log.info("DialogueServiceImpl->endDialogue: Ending dialogue for {}",uid);
        return redisUtils.deleteInHash(key,uid) ? ResultVo.success(Boolean.TRUE)
                : ResultVo.error(BusinessError.UNKNOWN_ERROR);
    }

    private SessionsSettings authDialogflow() {
        try {
            InputStream serviceAccount =
                    this.getClass().getClassLoader().getResourceAsStream("dialogflowServiceAccountKey.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();
            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            return settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        } catch (IOException e) {
            log.warn("DialogueServiceImpl->authDialogflow: IOException");
            e.printStackTrace();
        }
        return null;
    }

}

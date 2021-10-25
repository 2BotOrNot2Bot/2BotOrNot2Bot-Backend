package com.turing.test.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.turing.test.domain.Chatbot;
import com.turing.test.service.ChatbotService;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 16:00
**/
@Slf4j
@Service
public class ChatbotServiceImpl implements ChatbotService {
    public static final String COL_NAME="chatbots";

    @Override
    public CompletableFuture<ResultVo<Double>> updateChatbotStat(String name, Boolean result) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        final DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(name);

        log.info("ChatbotServiceImpl->updateChatbotStat: updating percentage of {}", name);

        ApiFuture<Double> futureTransaction = dbFirestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(documentReference).get();
            int newTestCount = ((Long) snapshot.get("testCount")).intValue();
            int newSuccessCount = ((Long) snapshot.get("successCount")).intValue();
            newTestCount++;
            if(result) newSuccessCount++;
            Double newPercentage = newSuccessCount*1.0/newTestCount;
            transaction.update(documentReference,"testCount", newTestCount);
            transaction.update(documentReference,"successCount", newSuccessCount);
            transaction.update(documentReference,"percentage", newPercentage);
            return newPercentage;
        });

        Double percentage = futureTransaction.get();
        if(percentage == null) return CompletableFuture.completedFuture(ResultVo.error(BusinessError.UNKNOWN_ERROR));

        log.info("ChatbotServiceImpl->updateChatbotStat: {} now has percentage of {}%", name,
                String.format("%.2f", percentage*100));

        return CompletableFuture.completedFuture(ResultVo.success(percentage));
    }

    @Override
    public ResultVo<List<Chatbot>> getSortedChatbotStat() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Create a reference to the collection
        CollectionReference chatbots = dbFirestore.collection("chatbots");
        // Create a query against the collection.
        Query query = chatbots.orderBy("percentage", Query.Direction.DESCENDING);
        // retrieve query results asynchronously using query.get()
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Chatbot> result = new ArrayList<>();
        if(querySnapshot.get().getDocuments().size()==0){
            log.error("ChatbotServiceImpl->getSortedChatbotStat: no chatbot found");
            return ResultVo.error(BusinessError.UNKNOWN_ERROR);
        }
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            result.add(document.toObject(Chatbot.class));
        }
        log.info("ChatbotServiceImpl->getSortedChatbotStat: found {} chatbots", result.size());
        return ResultVo.success(result);
    }

}

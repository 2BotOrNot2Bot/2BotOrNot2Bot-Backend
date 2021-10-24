package com.turing.test.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.turing.test.service.ChatbotService;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public ResultVo<Double> updateChatbotStat(String name, Boolean result) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(name);

        ApiFuture<Double> futureTransaction = dbFirestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(documentReference).get();
            Integer newTestCount = (Integer) snapshot.get("testCount") + 1;
            Integer newSuccessCount = (Integer) snapshot.get("successCount");
            if(result) newSuccessCount++;
            Double newPercentage = newSuccessCount.doubleValue()/newTestCount.doubleValue();
            transaction.update(documentReference,"testCount", newTestCount);
            transaction.update(documentReference,"successCount", newTestCount);
            transaction.update(documentReference,"percentage", newPercentage);
            return newPercentage;
        });

        log.info("ChatbotServiceImpl->updateChatbotStat: {} now has percentage of {}%", name,
                String.format("%.2f", futureTransaction.get()*100));

        return ResultVo.success(futureTransaction.get());
    }
}

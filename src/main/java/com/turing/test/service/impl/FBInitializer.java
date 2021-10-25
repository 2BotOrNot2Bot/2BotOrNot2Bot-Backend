package com.turing.test.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

/**
* @Author Yibo Wen
* @Date 2021/10/23 23:48
**/
@Slf4j
@Service
public class FBInitializer {

    @PostConstruct
    public void initialize() {
        try {
//            FileInputStream serviceAccount =
//                    new FileInputStream("");

            InputStream serviceAccount =
                    this.getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

            if(serviceAccount == null){
                log.error("FBInitializer->initialize: failed to find serviceAccountKey.json");
                throw new Exception();
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://botornot2bot-8e49c-default-rtdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

            log.info("FBInitialize->initialize: Firebase Realtime Database initialized");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
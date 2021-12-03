package com.turing.test.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.turing.test.domain.User;
import com.turing.test.service.UserService;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @Author Yibo Wen
 * @Date 2021/9/23 21:50
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    public static final String COL_NAME="users";

    public ResultVo<String> addUser(String firebaseUid) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        //check for duplicate user email
//        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(user.getEmail());
//        ApiFuture<DocumentSnapshot> future = documentReference.get();
//        DocumentSnapshot document = future.get();
//        if (document.exists()) {
//            return ResultVo.error(BusinessError.DUPLICATE_USER);
//        }

        //add user to firestore
        User user = new User();
        user.setUid(firebaseUid);
        user.setPoints(50); //initial points is 50
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(firebaseUid).set(user);
        log.info("UserServiceImpl->addUser: new user with uid {} added", firebaseUid);
        return ResultVo.success(collectionsApiFuture.get().getUpdateTime().toString());
    }

    public ResultVo<Integer> findUser(String firebaseUid) throws InterruptedException, ExecutionException {
        log.info("UserServiceImpl->findUser: finding user {}",firebaseUid);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(firebaseUid);
        double score = documentReference.get().get().getDouble("points");
        return ResultVo.success((int)score);
    }

    public ResultVo<Integer> updateUserPoints(String firebaseUid, Boolean answer) throws ExecutionException, InterruptedException {
        log.info("UserServiceImpl->updateUserPoints: user {} got the answer {}",firebaseUid, answer? "right" : "wrong");
        if(firebaseUid==null || firebaseUid.isEmpty()) return ResultVo.success(null);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        double score = answer ? 10.0 : -5.0;
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(firebaseUid);
        documentReference.update("points", FieldValue.increment(score)).get();
        score = documentReference.get().get().getDouble("points");
        log.info("UserServiceImpl->updateUserPoints: User Point Updated to {}",score);
        return ResultVo.success((int)score);
    }

    @Override
    public ResultVo<String> deleteUser(String firebaseUid) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(firebaseUid).delete();
        log.warn("UserServiceImpl->deleteUser: user {} deleted",firebaseUid);
        return ResultVo.success(collectionsApiFuture.get().getUpdateTime().toString());
    }
}

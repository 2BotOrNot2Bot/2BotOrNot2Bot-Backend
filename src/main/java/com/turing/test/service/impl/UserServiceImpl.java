package com.turing.test.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.turing.test.domain.User;
import com.turing.test.service.UserService;
import com.turing.test.service.dto.UserDto;
import com.turing.test.vo.BusinessError;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * @Author Yibo Wen
 * @Date 2021/9/23 21:50
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    public static final String COL_NAME="users";

    public ResultVo<String> addUser(User user) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        //check for duplicate user email
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(user.getEmail());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return ResultVo.error(BusinessError.DUPLICATE_USER);
        }

        //add user to firestore
        user.setPoints(0); //initial points is 0
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getEmail()).set(user);
        log.info("UserServiceImpl->addUser: new user {} added", user.getEmail());
        return ResultVo.success(collectionsApiFuture.get().getUpdateTime().toString());
    }

    public ResultVo<UserDto> findUser(String email) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(email);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        UserDto userDto = new UserDto();

        if(document.exists()) {
            User user = document.toObject(User.class);
            BeanUtils.copyProperties(user,userDto);
            return ResultVo.success(userDto);
        }else {
            return ResultVo.error(BusinessError.UNKNOWN_USER);
        }
    }
}

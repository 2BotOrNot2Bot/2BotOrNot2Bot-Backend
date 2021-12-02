package com.turing.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
* @Author Yibo Wen
* @Date 2021/10/23 23:10
**/
@Slf4j
@EnableCaching
@SpringBootApplication
public class BackendEntry {

	public static void main(String[] args) {
		SpringApplication.run(BackendEntry.class, args);
		log.info("======2BotOrNot2Bot back-end API is now running======");
	}

}

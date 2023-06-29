package com.sqs.examples.sqs.json;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class JsonConsumer {

	@SqsListener(value = "JsonQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void myConsumer(@Header(value="sender") String senderName, @Payload Employee employee) {
		System.out.println("Json Message sender:" + senderName);
		System.out.println("Json Message received:" + employee);
	} 
	
	@SqsListener(value = "success-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void mySuccessConsumer(String message) {
		System.out.println("Success messaged received: " + message);
		
	}
	
	@SqsListener(value = "failure-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void myFailureConsumer(String message) {
		System.out.println("Failed messaged received: " + message);
		
	}
 
}

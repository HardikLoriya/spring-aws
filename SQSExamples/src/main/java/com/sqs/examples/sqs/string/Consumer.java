package com.sqs.examples.sqs.string;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

	@SqsListener(value = "FirstQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void myConsumer(@Header("sender") String senderName, String message) {
		System.out.println("Message sender:" + senderName);
		System.out.println("Message received:" + message);
	} 
	
}

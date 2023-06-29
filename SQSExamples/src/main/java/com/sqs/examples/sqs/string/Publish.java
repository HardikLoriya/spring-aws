
package com.sqs.examples.sqs.string;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Publish {


	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	
	@Value("${cloud.aws.sqs.url}")
	private String sqsUrl;

	@PostMapping("/publish/{msg}")
	public ResponseEntity<String> postMessage(@PathVariable("msg") String message) {
		
		Message<String> msg = MessageBuilder.withPayload(message)
			      .setHeader("sender", "loriya")
			      .setHeaderIfAbsent("country", "AE")
			      .build();
		queueMessagingTemplate.send(sqsUrl, msg);
		return new ResponseEntity<>("Message Published", HttpStatus.OK);
	}
	
	
}


package com.sqs.examples.sqs.json;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class JsonPublish {

	@Autowired
	JsonAWSSQSConfig awssqsConfig_Object;

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	
	@Value("${cloud.aws.sqs.url}")
	private String sqsUrl;

	@PostMapping("/publish")
	public ResponseEntity<String> postEmployeeMessage(@RequestBody Employee employee) {
		String sqsUrl = awssqsConfig_Object.getQueryURL();
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("sender", "hardik");
		queueMessagingTemplate.convertAndSend(sqsUrl, employee,headerMap);
		return new ResponseEntity<>("Message Published", HttpStatus.OK);
	}

}

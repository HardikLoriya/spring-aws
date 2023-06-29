package com.sqs.examples.sqs.json;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.HeaderMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.QueueNameExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonAWSSQSConfig {
	
	private Logger log = LoggerFactory.getLogger(JsonAWSSQSConfig.class);

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Autowired
	ObjectMapper mapper;
	
	private String queryUrl;

	private AmazonSQSAsync buildAmazonSQSAsync() {

		return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();

	}
	
	@PostConstruct
	public void createSQSMsgQueue() {
		log.info("Creating message queue on AWS SQS");
		CreateQueueRequest request = new CreateQueueRequest();
		request.setQueueName("JsonQueue");
		try {
			CreateQueueResult queue = buildAmazonSQSAsync().createQueue(request);
			log.info("Create queue response : {}",queue.getQueueUrl());
			queryUrl = queue.getQueueUrl();
		} catch(QueueNameExistsException e) {
			log.error("Queue name Exists {}",e.getMessage());
		}
	}
	
	public String getQueryURL() {
		return queryUrl; 
	}

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate() {
		return new QueueMessagingTemplate(buildAmazonSQSAsync());
	}

	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory(final ObjectMapper mapper) {

		final QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
		factory.setAmazonSqs(buildAmazonSQSAsync());
		factory.setArgumentResolvers(Arrays.asList(new HeaderMethodArgumentResolver(null, null),
				new PayloadMethodArgumentResolver(myConverter(mapper))
				));
		return factory;
	}

	private MessageConverter myConverter(final ObjectMapper mapper) {

		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(mapper);
		return converter;
	}

}

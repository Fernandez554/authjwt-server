package com.nttbank.microservices.authjwtserver.util;

import com.nttbank.microservices.commonlibrary.event.GenericEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaUtil {

  private final KafkaTemplate<String, GenericEvent> kafkaTemplate;

//  @Value("${kafka.nttbank.topic:nttbank}")
  private String topicName = "account-service-management-v1";

  public void sendMessage(GenericEvent obj) {
    log.info("Sending message to the topic " + topicName);
    kafkaTemplate.send(topicName, obj);
  }

}

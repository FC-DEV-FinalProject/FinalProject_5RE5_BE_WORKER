package com.oreo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.MessageAttribute;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.AmazonSQSResponderClientBuilder;
import com.amazonaws.services.sqs.MessageContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.tts.dto.AudioOptionDto;
import com.oreo.tts.dto.TtsSentenceDto;
import com.oreo.tts.dto.VoiceDto;
import com.oreo.tts.dto.request.TtsMakeRequest;
import com.oreo.tts.dto.response.TtsMakeResponse;
import com.oreo.tts.service.TtsMakeService;
import com.oreo.util.RequestSerializer;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainHandler implements RequestHandler<SQSEvent, String> {

    private static final Logger log = Logger.getLogger("com.oreo.MainHandler");
    private static final TtsMakeService ttsMakeService = TtsMakeService.getInstance();
    private static final ObjectMapper mapper = RequestSerializer.getInstance();

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        log.log(Level.SEVERE, "[handleRequest] evnet - "+ event+ " | context - " + context);
        AmazonSQSResponder sqs = AmazonSQSResponderClientBuilder.defaultClient();
        log.log(Level.SEVERE, "[handleRequest] sqs - "+ sqs);
            for (SQSMessage msg : event.getRecords()) {
                log.log(Level.SEVERE, "[handleRequest] msg - " + msg);
                processMessage(sqs, msg, context);
            }
        return "Hello from Lambda!";
    }

    private void processMessage(AmazonSQSResponder sqs, SQSMessage sqsMessage, Context context){
        context.getLogger();
        log.log(Level.INFO, "context : " + context);
        Message message = Message.builder()
            .body(sqsMessage.getBody())
            .messageAttributes(sqsMessage.getMessageAttributes().entrySet().stream().collect(
                Collectors.toMap(Entry::getKey,
                    entry -> toMessageAttributeValue(entry.getValue()))))
            .build();
        MessageContent requestMessage = MessageContent.fromMessage(message);

        log.log(Level.INFO, "message : " + message.toString());
        log.log(Level.INFO, "message.body() : " + message.body());
        log.log(Level.INFO, "message.messageAttributes() " + message.messageAttributes().toString());
        log.log(Level.INFO, "message.messageAttributes().get(\"messageType\") : " + message.messageAttributes().keySet());

        Object routerResponse  = router(message);
        log.log(Level.INFO, "routerreponse: " + routerResponse.toString());

        MessageContent response = new MessageContent(sqsMessage.getBody());

        sqs.sendResponseMessage(requestMessage, response);
    }

    private Object router(Message message) {
        String messageType = message.attributes().get("messageType");

        if (messageType.equals("TTS_MAKE")) {
            VoiceDto voiceD = new VoiceDto("ko-KR", "ko-KR-Standard-C", "female");
            AudioOptionDto audioOptionD = new AudioOptionDto(1.0f, 0.0f, 100);
            TtsSentenceDto ttsSentenceDto = new TtsSentenceDto("안녕하세요", voiceD, audioOptionD);
            TtsMakeRequest ttsMakeRequest = new TtsMakeRequest(ttsSentenceDto, "lambdaTest");
            TtsMakeResponse ttsMakeResponse = ttsMakeService.makeTtsAndUploadS3(ttsMakeRequest);
            return ttsMakeResponse;
        }
        throw new IllegalArgumentException("Unknown message type: " + messageType);
    }


    private MessageAttributeValue toMessageAttributeValue(MessageAttribute attribute) {
        return MessageAttributeValue.builder()
            .dataType(attribute.getDataType())
            .stringValue(attribute.getStringValue())
            .stringListValues(attribute.getStringListValues())
//            .binaryListValues(attribute.getBinaryListValues())
            .build();
    }

}


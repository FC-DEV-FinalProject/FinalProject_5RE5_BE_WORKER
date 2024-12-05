package com.oreo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.MessageAttribute;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.AmazonSQSResponderClientBuilder;
import com.amazonaws.services.sqs.MessageContent;
import com.oreo.tts.dto.AudioOptionDto;
import com.oreo.tts.dto.TtsSentenceDto;
import com.oreo.tts.dto.VoiceDto;
import com.oreo.tts.dto.request.TtsMakeRequest;
import com.oreo.tts.dto.response.TtsMakeResponse;
import com.oreo.tts.service.TtsMakeService;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

public class MainHandler implements RequestHandler<SQSEvent, String> {

    private static final Logger log = LoggerFactory.getLogger(MainHandler.class);
    private static final TtsMakeService ttsMakeService = TtsMakeService.getInstance();

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        AmazonSQSResponder sqs = AmazonSQSResponderClientBuilder.defaultClient();

        for (SQSMessage msg : event.getRecords()) {
            processMessage(sqs, msg, context);
        }

        return "Hello from Lambda!";
    }

    private void processMessage(AmazonSQSResponder sqs, SQSMessage sqsMessage, Context context) {

        System.out.println("sqsMessage.getBody() = " + sqsMessage.getBody());
        System.out.println("context.getFunctionName() = " + context.getFunctionName());
        context.getLogger();

        Message message = Message.builder()
            .body(sqsMessage.getBody())
            .messageAttributes(sqsMessage.getMessageAttributes().entrySet().stream().collect(
                Collectors.toMap(Entry::getKey,
                    entry -> toMessageAttributeValue(entry.getValue()))))
            .build();

        MessageContent requestMessage = MessageContent.fromMessage(message);

        System.out.println("message.getBody() = " + message.body());
        Object routerResponse  = router(message);
        log.info("routerreponse : {} ", routerResponse);

        log.info("messageType: {}", message.attributes().get("messageType"));

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

            log.info("ttsMakeRequest : {} ", ttsMakeRequest);

            TtsMakeResponse ttsMakeResponse = ttsMakeService.makeTtsAndUploadS3(ttsMakeRequest);

            log.info("ttsMakeResponse : {} ", ttsMakeResponse);
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


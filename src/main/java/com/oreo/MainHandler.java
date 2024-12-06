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
import com.oreo.tts.dto.request.TtsMakeRequest;
import com.oreo.tts.dto.response.TtsMakeResponse;
import com.oreo.tts.service.TtsMakeService;
import com.oreo.util.RequestSerializer;
import com.oreo.vc.VcAPIResult;
import com.oreo.vc.VcRequestDto;
import com.oreo.vc.VcResultDto;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sound.sampled.UnsupportedAudioFileException;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

public class MainHandler implements RequestHandler<SQSEvent, String> {

    private static final Logger log = Logger.getLogger("com.oreo.MainHandler");
    private static final TtsMakeService ttsMakeService = TtsMakeService.getInstance();
    private static final ObjectMapper mapper = RequestSerializer.getInstance();

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        log.log(Level.SEVERE, "[handleRequest] evnet - "+ event+ " | context - " + context);
        AmazonSQSResponder sqs = AmazonSQSResponderClientBuilder.defaultClient();
        log.log(Level.SEVERE, "[handleRequest] sqs - "+ sqs);

        // sqs message를 들고 온다.
        for (SQSMessage sqsMessage : event.getRecords()) {
            log.log(Level.SEVERE, "[handleRequest] msg - " + sqsMessage);

            // request Message 생성
            Message requestMessage = toMessage(sqsMessage);

            // sendResponseMessage 를 하기 위해서 MessageContent로 변환
            MessageContent requestMessageContent = MessageContent.fromMessage(requestMessage);

            try {
                // message를 처리한다.
                MessageContent reponseMessageContent = processMessage(requestMessage);
                // sendResponseMessage 호출 => 이 결과가 다시 SQS에 들어간다.
                sqs.sendResponseMessage(requestMessageContent, reponseMessageContent);
            } catch (UnsupportedAudioFileException | IOException e) {
                log.log(Level.WARNING, "Error processing message", e);
                MessageContent errorResponse = new MessageContent(e.getMessage());
                sqs.sendResponseMessage(requestMessageContent, errorResponse);
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    private MessageContent processMessage(Message requestMessage)
            throws UnsupportedAudioFileException, IOException {
        log.log(Level.INFO, "message.messageAttributes().get(\"messageType\").stringValue();" + requestMessage.messageAttributes().get("messageType").stringValue());

        // router 실제 처리할 메서드
        Object routerResponse  = router(requestMessage);

        log.log(Level.INFO, "router reponse: " + routerResponse.toString());

        // 결과 객체 json 형태로 변환
        String jsonResponse = mapper.writeValueAsString(routerResponse);

        // response message 생성
        return new MessageContent(jsonResponse);
    }

    private Object router(Message message) throws UnsupportedAudioFileException, IOException {
        log.log(Level.SEVERE, "[MainHandler] router - message : "+ message.toString());
        Map<String, MessageAttributeValue> messageAttributes = message.messageAttributes();

        log.log(Level.SEVERE, "[MainHandler] router - messageAttributes : "+ messageAttributes.toString());
        if (messageAttributes.containsKey("messageType")) {
            String stringValue = messageAttributes.get("messageType").stringValue();
            if (stringValue.equals("TTS_MAKE")) {
                // TTS 생성 요청
                TtsMakeRequest ttsMakeRequest = TtsMakeRequest.of(mapper, message.body());

                // 생성 로직
                TtsMakeResponse ttsMakeResponse = ttsMakeService.makeTtsAndUploadS3(ttsMakeRequest);
                return ttsMakeResponse;
            }
            if(stringValue.equals("VC_MAKE")){
                VcRequestDto vcApiRequest = mapper.readValue(message.body(), VcRequestDto.class);
                //messageAttributes를 파싱해서 넣어줘야한다.
                List<String> srcUrls = vcApiRequest.getSrcUrls();
                String trgUrl = vcApiRequest.getTrgUrl();
                VcRequestDto vcRequestDto = new VcRequestDto(srcUrls, trgUrl);//여기에 들어오는 값을 넣고

//                String trg = VcAPIResult.trg(vcRequestDto.getTrgUrl());
                List<VcResultDto> result = VcAPIResult.result(vcRequestDto.getSrcUrls(),
                        VcAPIResult.trg(vcRequestDto.getTrgUrl()));

                return result;
            }
            throw new IllegalArgumentException("Unknown message type: " + messageAttributes);
        }
        throw new IllegalArgumentException("Unknown message type: " + messageAttributes);
    }

    private Message toMessage (SQSMessage sqsMessage) {
        // SQS message를 Message로 변환
        return Message.builder()
            .body(sqsMessage.getBody())
            .messageAttributes(sqsMessage.getMessageAttributes().entrySet().stream().collect(
                Collectors.toMap(Entry::getKey,
                    entry -> toMessageAttributeValue(entry.getValue()))))
            .build();
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


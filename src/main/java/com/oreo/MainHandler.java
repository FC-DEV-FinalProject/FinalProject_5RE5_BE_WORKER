package com.oreo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.MessageAttribute;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.services.sqs.AmazonSQSResponder;
import com.amazonaws.services.sqs.AmazonSQSResponderClientBuilder;
import com.amazonaws.services.sqs.MessageContent;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

public class MainHandler implements RequestHandler<SQSEvent, String> {

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

        MessageContent response = new MessageContent(sqsMessage.getBody());

        sqs.sendResponseMessage(requestMessage, response);

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


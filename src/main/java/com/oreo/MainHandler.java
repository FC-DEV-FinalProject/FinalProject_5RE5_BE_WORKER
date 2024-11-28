package com.oreo;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

public class MainHandler implements RequestHandler<SQSEvent, String> {

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        for (SQSMessage msg : event.getRecords()) {
            processMessage(msg, context);
        }

        return "Hello from Lambda!";
    }

    private void processMessage(SQSMessage event, Context context) {
        try {
            context.getLogger().log("Received event: " + event);

        } catch (Exception e) {
            context.getLogger().log(e.getMessage());
        }
    }

}


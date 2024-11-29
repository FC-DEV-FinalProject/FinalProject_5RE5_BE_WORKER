FROM public.ecr.aws/lambda/java:17
COPY build/libs/FinalProject_5RE5_BE-0.0.1-SNAPSHOT.jar app.jar
CMD [ "com.oreo.MainHandler::handleRequest" ]
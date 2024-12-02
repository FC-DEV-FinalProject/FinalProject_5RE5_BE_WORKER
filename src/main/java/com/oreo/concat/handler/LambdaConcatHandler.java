package com.oreo.concat.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.oreo.concat.concatenator.dto.AudioProperties;
import com.oreo.concat.concatenator.dto.*;
import com.oreo.concat.concatenator.model.AudioInfo;
import com.oreo.concat.concatenator.model.ConcatHandlerService;
import com.oreo.concat.concatenator.model.ConcatenatorFactory;
import com.oreo.concat.concatenator.model.ResultStorage;
import com.oreo.s3.S3Service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

public class LambdaConcatHandler implements RequestHandler<LambdaConcatRequest, ResultStorage<LambdaConcatResultDto>> {
    private final S3Service s3Service = new S3Service();
    private static final String processId = UUID.randomUUID().toString().substring(0, 8);
    private final ResultStorage<LambdaConcatResultDto> resultStorage = new ResultStorage<>();

    @Override
    public ResultStorage<LambdaConcatResultDto> handleRequest(LambdaConcatRequest lambdaRequest, Context context) {
        List<ConcatRowRequest> audios = lambdaRequest.getAudios();
        AudioFormat audioFormat = lambdaRequest.getAudioFormatDto().toAudioFormat();

        ConcatHandlerService concatHandlerService = new ConcatHandlerService(ConcatenatorFactory.of(audioFormat));
        List<AudioProperties> audioProperties;

        int i = 1;
        do {
            audioProperties = concatHandlerService.loadAudios(audios);//오디오 지정한 용량 만큼 불러오기
            if (audioProperties.isEmpty()) {
                break;
            }
            ConcatTabResponseDto concatTabResponseDto = lambdaRequest.getConcatTabResponseDto();
            ByteArrayOutputStream byteArrayOutputStream = concatHandlerService.concat(audioProperties,
                    concatTabResponseDto.getFrontSilence());//불러온 오디오 병합하기

            AudioInputStream resample = concatHandlerService.resample(byteArrayOutputStream);
            //오디오를 AudioInputStream으로 변환

            //# S3저장하기
            AudioInfo info = new AudioInfo(resample,
                    lambdaRequest.getFileName(),
                    resample.getFrameLength() / audioFormat.getFrameRate(),
                    "audio/wav");
            String s3Url = s3Service.uploadSingleFile(info);

            resultStorage.add(new LambdaConcatResultDto(info, s3Url, "wav", i++, processId));
        }
        while (!audioProperties.isEmpty());

        return resultStorage;
    }

//    public static void main(String[] args) {
//        ConcatOptionDto concatOptionDto = new ConcatOptionDto(1L, "test");
//        ConcatTabResponseDto concatTabResponseDto = new ConcatTabResponseDto(1L, concatOptionDto, 0.0f, 'Y');
//        AudioFormatDto audioFormatDto = new AudioFormatDto(44100f, 16, 2, 4, 44100f);
//        String url = "https://5re5park-s3-audiofile.s3.ap-northeast-2.amazonaws.com/concat/audio/07fe00c8-906b-4b77-8e25-a591109aeef6_2b2d1214-901c-47e2-b1a0-9f4d13573481_project-1-tts-1.wav";
//        OriginAudioRequest originAudioRequest = new OriginAudioRequest(1L, url, "wav", 0L, 0L, "file");
//        ConcatRowRequest concatRowRequest = new ConcatRowRequest(1L, originAudioRequest, "test'< ", 'Y', 0.0f, 1, 'Y');
//        String fileName = "testFile";
//
//        LambdaConcatRequest lambdaConcatRequest = new LambdaConcatRequest(concatTabResponseDto, audioFormatDto, List.of(concatRowRequest, concatRowRequest, concatRowRequest), fileName);
//
//        LambdaConcatHandler lambdaConcatHandler = new LambdaConcatHandler();
//
//        lambdaConcatHandler.handleRequest(lambdaConcatRequest);
//
//    }
}


// 1. 데이터 요청
// 2. 각 DTO 꺼내기
// 3. 지정한 용량만큼 데이터 불러오기
// 4. 병합
// 5. s3 저장
// 6. url 반환 수령
// 7. ConcatResultDto(크기, 길이, url, 확장자) 리스트로 반환
// 8. 요청받은 오디오가 없을 때까지 2~7 반복

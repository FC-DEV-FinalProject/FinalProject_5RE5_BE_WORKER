package com.oreo.tts.service;

import com.oreo.concat.concatenator.model.AudioInfo;
import com.oreo.s3.S3Service;
import com.oreo.tts.client.AudioConfigGenerator;
import com.oreo.tts.client.GoogleTTSService;
import com.oreo.tts.client.SynthesisInputGenerator;
import com.oreo.tts.client.VoiceParamsGenerator;
import com.oreo.tts.dto.TtsSentenceDto;
import com.oreo.tts.dto.request.TtsMakeRequest;
import com.oreo.tts.dto.response.TtsMakeResponse;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TtsMakeService {
    private static final GoogleTTSService googleTTSService = new GoogleTTSService();
    private static final S3Service s3Service = new S3Service();
    // singleton
    private static final TtsMakeService instance = new TtsMakeService();

    private TtsMakeService () {}
    public static TtsMakeService getInstance() {
        return instance;
    }

    public TtsMakeResponse makeTtsAndUploadS3(TtsMakeRequest ttsMakeRequest) {
        try{
            // 입력 값 검증(예정)
            TtsSentenceDto ttsSentence = ttsMakeRequest.getTtsSentence();

            // google TTS api 수행
            byte[] ttsResult = googleTTSService.make(
                    SynthesisInputGenerator.generate(ttsSentence.getText()),
                    VoiceParamsGenerator.of(ttsSentence.getVoice()),
                    AudioConfigGenerator.of(ttsSentence.getAudioOption())
            );

            // TTS 결과(byte[])를 AudioInputStream으로 변환
            InputStream inputStream = new ByteArrayInputStream(ttsResult);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

            // TTS 결과 AudioInfo 객체 생성
            AudioInfo ttsAudioInfo = new AudioInfo(
                    audioInputStream,
                    ttsMakeRequest.getFileName() + ".wav",
                    audioInputStream.getFrameLength(),
                    "audio/wav"
            );

            // TTS 결과 데이터 s3에 업로드
            String s3Url = s3Service.uploadSingleFile(ttsAudioInfo, "tts");


            // TTS 생성 결과 응답
            return new TtsMakeResponse(
                    ttsAudioInfo,
                    s3Url
            );
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException("AudioInputStream 변환 중 예외 발생");
        }
    }
}

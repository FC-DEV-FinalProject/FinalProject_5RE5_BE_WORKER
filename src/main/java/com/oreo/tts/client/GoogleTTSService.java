package com.oreo.tts.client;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.oreo.tts.config.GoogleTTSConfig;

public class GoogleTTSService {
    private static final TextToSpeechClient ttsClient = GoogleTTSConfig.getTTSClient();

    public byte[] make(SynthesisInput input, VoiceSelectionParams voice, AudioConfig audioConfig) {
        if(checkInvalidParams(input, voice, audioConfig)) {
            throw new IllegalArgumentException("TTS 요청 파라미터가 부족합니다");
        }

        // tts 요청
        SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);

        // 응답으로부터 오디오 컨텐츠 얻기
        ByteString audioContents = response.getAudioContent();

        return audioContents.toByteArray();

    }

    // tts 생성에 필요한 파라미터들 null 아닌지 검사
    private boolean checkInvalidParams(SynthesisInput input, VoiceSelectionParams voice, AudioConfig audioConfig) {
        return input == null || voice == null || audioConfig == null;
    }
}

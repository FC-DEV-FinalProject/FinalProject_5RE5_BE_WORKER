package com.oreo.tts.client;

import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.oreo.tts.dto.VoiceDto;

public class VoiceParamsGenerator {

    // 언어 코드, 보이스명, 성별 값을 가지고 VoiceSelectionParmas 객체 생성
    public static VoiceSelectionParams generate(String languageCode, String voiceName, String gender) {
        if(checkParamsNull(languageCode, voiceName, gender)) {
            throw new IllegalArgumentException("보이스 객체 생성을 위한 파라미터가 부족합니다.");
        }

        return VoiceSelectionParams.newBuilder()
                .setLanguageCode(languageCode)
                .setName(voiceName)
                .setSsmlGender(convertToSsmlVoiceGender(gender))
                .build();
    }

    public static VoiceSelectionParams of(VoiceDto voiceDto) {
        return generate(voiceDto.getLangCode(), voiceDto.getName(), voiceDto.getGender());
    }


    // 성별 값(string)을 SsmlVoiceGender 객체로 변환하는 메서드
    private static SsmlVoiceGender convertToSsmlVoiceGender(String gender) {
        try {
            return SsmlVoiceGender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("SsmlVoiceGender 객체로 변환할 수 없는 성별 값 입니다.");
        }

    }

    // 파라미터값 null 아닌지 검증
    private static boolean checkParamsNull(String languageCode, String voiceName, String gender) {
        return languageCode == null || voiceName == null || gender == null;
    }
}

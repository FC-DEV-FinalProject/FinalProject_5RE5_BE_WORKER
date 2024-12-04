package com.oreo.tts.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GoogleTTSConfig {
    private static final String GOOGLE_CREDENTIALS_JSON = System.getenv("GOOGLE_CREDENTIALS_JSON");

    public static TextToSpeechClient getTTSClient() {
        try {
            // 세팅 객체에 인증 정보 등록
            TextToSpeechSettings ttsSettings =
                    TextToSpeechSettings.newBuilder()
                            .setCredentialsProvider(() -> createCredentials())
                            .build();

            // 세팅 정보 전달하며 TTSClient 객체 생성
            return TextToSpeechClient.create(ttsSettings);
        } catch (IOException e) {
            throw new RuntimeException("Google TTS Client 객체 생성 중 문제 발생", e);
        }

    }

    private static GoogleCredentials createCredentials() throws IOException {
        return GoogleCredentials.fromStream(
                new ByteArrayInputStream(GOOGLE_CREDENTIALS_JSON.getBytes())
        );
    }
}

package com.oreo.vc;


import com.oreo.concat.concatenator.model.AudioInfo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VcAPIService {
    private static final Logger LOGGER = Logger.getLogger("com.oreo.vc.VcApiService");

    private static final String vcUrl = System.getenv("VC_URL");
    private static final String vcApiKey = System.getenv("VC_APIKEY");

    private static final String REMOVE_BACKGROUND_NOISE = "remove_background_noise"; // 백그라운드 노이즈 제거 설정 키
    private static final String RESPONSE_FILENAME = "response.wav"; // 응답 파일 이름
    private static final String RESPONSE_CONTENT_TYPE = "audio/wav"; // 응답 파일 MIME 타입

    /**
     * trgId 생성 요청 API
     * @param file
     * @return
     */
    public String trgIdCreate(AudioInfo file) {
        String url = vcUrl + "/voices/add";
        try {
            HttpURLConnection connection = createHttpPost(url);
            LOGGER.log(Level.INFO, "[trgIdCreate] Preparing to send request");

            // 요청 데이터 생성
            String boundary = UUID.randomUUID().toString();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream outputStream = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {

                // Multipart 데이터 작성
                addFormField(writer, boundary, "name", "5re5PARKTRG");
                addFormField(writer, boundary, REMOVE_BACKGROUND_NOISE, "true");
                addFilePart(writer, outputStream, boundary, "files", file);

                // 끝 경계 작성
                writer.append("--").append(boundary).append("--").append("\r\n").flush();
            }

            // 응답 처리
            String responseBody = readResponse(connection);
            LOGGER.log(Level.INFO, "[trgIdCreate] Response: " + responseBody);

            // trgId 추출
            String trgId = extractValue(responseBody, "voice_id");
            if (trgId == null) {
                throw new IllegalArgumentException("voice_id is null");
            }
            return trgId;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create trgId", e);
        }
    }

    /**
     * resultFile 생성 API
     * @param files
     * @param trgId
     * @return
     */
    public List<AudioInfo> resultFileCreate(List<AudioInfo> files, String trgId) {
        LOGGER.log(Level.INFO, "[resultFileCreate] Input files: " + files.toString() + " | trgId: " + trgId);
        List<AudioInfo> resultFiles = new ArrayList<>();
        for (AudioInfo file : files) {
            if (file == null) {
                throw new IllegalArgumentException("File is empty");
            }
            AudioInfo resultFile = createResultFile(file, trgId);
            resultFiles.add(resultFile);
        }
        return resultFiles;
    }

    // 결과 파일 생성 API 요청
    private AudioInfo createResultFile(AudioInfo file, String trgId) {
        String url = vcUrl + "/speech-to-speech/" + trgId + "?output_format=mp3_44100_192";

        try {
            HttpURLConnection connection = createHttpPost(url);

            String boundary = UUID.randomUUID().toString();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream outputStream = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {

                addFilePart(writer, outputStream, boundary, "audio", file);
                addFormField(writer, boundary, REMOVE_BACKGROUND_NOISE, "true");

                writer.append("--").append(boundary).append("--").append("\r\n").flush();
            }

            InputStream contentStream = connection.getInputStream(); // 일반 InputStream
            LOGGER.log(Level.INFO, "[createResultFile] contentStream 확인 : " + contentStream);


            // 응답 처리
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(contentStream);
            LOGGER.log(Level.INFO, "[createResultFile] audioInputStream 확인 : " + audioInputStream);
            return new AudioInfo(audioInputStream,
                    RESPONSE_FILENAME,
                    audioInputStream.getFrameLength(),
                    RESPONSE_CONTENT_TYPE);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create result file", e);
        }
    }

    // HttpURLConnection 생성
    private HttpURLConnection createHttpPost(String url) throws IOException {
        URL endpoint = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("xi-api-key", vcApiKey);
        return connection;
    }

    // Multipart Form 필드 추가
    private void addFormField(PrintWriter writer, String boundary, String name, String value) {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n");
        writer.append("Content-Type: text/plain; charset=UTF-8\r\n\r\n");
        writer.append(value).append("\r\n");
    }

    // Multipart 파일 추가
    private void addFilePart(PrintWriter writer, OutputStream outputStream, String boundary, String fieldName, AudioInfo file) throws IOException {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(file.getFileName()).append("\"\r\n");
        writer.append("Content-Type: ").append(file.getContentType()).append("\r\n\r\n");
        writer.flush();

        InputStream fileInputStream = file.toInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        fileInputStream.close();

        writer.append("\r\n");
        writer.flush();
    }

    // 응답 읽기
    private String readResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream();
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    // JSON 데이터에서 특정 키의 값을 추출
    private static String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\":\"";
        int startIndex = json.indexOf(keyPattern) + keyPattern.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (startIndex <= keyPattern.length() || endIndex <= 0) {
            return null;
        }
        return json.substring(startIndex, endIndex);
    }
}
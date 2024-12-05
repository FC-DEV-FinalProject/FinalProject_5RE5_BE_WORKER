package com.oreo.vc;


import com.oreo.concat.concatenator.model.AudioInfo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = createHttpPost(url);
            LOGGER.log(Level.INFO,"[trgIdeCreate] TEXT_PLAIN 요청값 확인 : "+ ContentType.TEXT_PLAIN);
            LOGGER.log(Level.INFO,"[trgIdeCreate] toInputStream 요청값 확인 : "+ file.toInputStream());
            LOGGER.log(Level.INFO,"[trgIdeCreate] getContentType 요청값 확인 : "+ file.getContentType());
            LOGGER.log(Level.INFO,"[trgIdeCreate] getFileName 요청값 확인 : "+ file.getFileName());

            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .addTextBody("name", "5re5PARKTRG", ContentType.TEXT_PLAIN) // 요청 이름 설정
                    .addBinaryBody("files", file.toInputStream(),
                            ContentType.create(file.getContentType()),
                            file.getFileName()) // 파일 데이터 추가
                    .addTextBody(REMOVE_BACKGROUND_NOISE, "true", ContentType.TEXT_PLAIN); // 노이즈 제거 옵션 설정
            LOGGER.log(Level.INFO,"[trgIdeCreate] builder 요청값 확인 : " + builder.toString());

            post.setEntity(builder.build());
            String responseBody = executeRequest(httpClient, post);// API 요청 실행
            LOGGER.log(Level.INFO,"[trgIdeCreate] responseBody 요청값 확인 : " + responseBody);
            //trgID 추출
            String trgId = extractValue(responseBody, "voice_id");
            LOGGER.log(Level.INFO,"[trgIdeCreate] trgId 요청값 확인 : " + trgId);

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
        LOGGER.log(Level.INFO,"[resultFileCreate]  매개변수값 확인 : " + files.toString() + " | " + trgId);
        List<AudioInfo> resultFiles = new ArrayList<>();
        for (AudioInfo file : files) {
            if (file.equals(null)) {
                throw new IllegalArgumentException("File is empty");
            }
            AudioInfo resultFile = createResultFile(file, trgId);
            LOGGER.log(Level.INFO,"[resultFileCreate]  resultFile 확인 : " + resultFile);
            resultFiles.add(resultFile);
        }
        LOGGER.log(Level.INFO,"[resultFileCreate]  resultFile 확인 : " + resultFiles.toString());
        return resultFiles;
    }

    //결과 파일 생성 api 요청
    private AudioInfo createResultFile(AudioInfo file, String trgId) {
        LOGGER.log(Level.INFO,"[createResultFile]  매개변수값 확인 : " + file.toString() + " | " + trgId);

        String url = vcUrl + "/speech-to-speech/" + trgId + "?output_format=mp3_44100_192";
        LOGGER.log(Level.INFO,"[createResultFile]  url 확인 : " + url );

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = createHttpPost(url);
            LOGGER.log(Level.INFO,"[createResultFile]  post 확인 : " + post );

            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .addBinaryBody("audio", file.toInputStream(),
                            ContentType.create(file.getContentType()),
                            file.getFileName()) // 파일 데이터 추가
                    .addTextBody(REMOVE_BACKGROUND_NOISE, "true", ContentType.TEXT_PLAIN); // 노이즈 제거 옵션 설정
            LOGGER.log(Level.INFO,"[createResultFile]  builder 확인 : " + builder );

            post.setEntity(builder.build());

            try (CloseableHttpResponse response = httpClient.execute(post)) {
                InputStream contentStream = response.getEntity().getContent(); // 일반 InputStream
                LOGGER.log(Level.INFO, "[createResultFile] contentStream 확인 : " + contentStream);

                // InputStream을 AudioInputStream으로 변환
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(contentStream);

                LOGGER.log(Level.INFO, "[createResultFile] audioInputStream 확인 : " + audioInputStream);

                // AudioInputStream을 사용해 AudioInfo 객체 생성
                return new AudioInfo(
                        audioInputStream,
                        RESPONSE_FILENAME,
                        audioInputStream.getFrameLength(),
                        RESPONSE_CONTENT_TYPE
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create result file", e);
        }
    }
    //HttpPost 객체 생성
    private HttpPost createHttpPost(String url) {
        HttpPost post = new HttpPost(url);
        post.setHeader("xi-api-key", vcApiKey);
        return post;
    }

    //API 요청 실행
    private String executeRequest(CloseableHttpClient httpClient, HttpPost post) throws Exception {
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = new String(response.getEntity().getContent().readAllBytes()); // 응답
            if (statusCode >= 400) {
                throw new IllegalArgumentException("Request failed with status code: " + statusCode);
            }
            return responseBody;
        }
    }
    //JSON 데이터에서 특정 키의 값을 추출
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

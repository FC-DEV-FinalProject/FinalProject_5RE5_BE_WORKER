package com.oreo.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.oreo.concat.concatenator.audio.AudioExtensionConverter;
import com.oreo.concat.concatenator.model.AudioInfo;
import com.oreo.s3.config.S3Config;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class S3Service {
    private static final Logger LOGGER = Logger.getLogger("com.oreo.vc.S3Service");
    private static final String AWS_S3_BUKET_NAME = System.getenv("AWS_S3_BUKET_NAME");
    private final AmazonS3 s3Client;
    public S3Service() {
        s3Client = S3Config.amazonS3Client();
    }

    public List<String> upload(List<AudioInfo> files, String dirName) {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다");
        }

        List<String> uploadedUrls = new ArrayList<>();
        for (AudioInfo file : files) {
            uploadedUrls.add(uploadSingleFile(file, dirName));
        }
        return uploadedUrls;
    }

    // 공통 업로드 로직을 처리하는 메서드
    public String uploadSingleFile(AudioInfo file, String dirName) {
        // 버킷 내 저장 경로(key) 설정
        String key = generateFileKey(dirName, file.getFileName());

        // 객체 추가 요청 정보 초기화
        PutObjectRequest request = createPutObjectRequest(file, key);

        // S3 버킷에 객체 추가
        s3Client.putObject(request);

        // 업로드한 파일의 S3 URL 반환
        return s3Client.getUrl(AWS_S3_BUKET_NAME, key).toString();
    }

    // 파일의 키 생성
    private String generateFileKey(String dirName, String originalFilename) {
        return dirName + "/" + UUID.randomUUID() + "_" + originalFilename;
    }

//    // PutObjectRequest 생성
    private PutObjectRequest createPutObjectRequest(AudioInfo file, String key) {
        return new PutObjectRequest(
                AWS_S3_BUKET_NAME,
                key,
                file.toInputStream(),
                createObjectMetadata(file)
        );
    }






    public String uploadSingleFile(AudioInfo info) {
        if (info == null) {
            throw new IllegalArgumentException("파일이 없습니다");
        }

        // 객체 추가 요청 정보 초기화
        PutObjectRequest request = createPutObjectRequest(info);

        // S3 버킷에 객체 추가
        s3Client.putObject(request);

        // 업로드한 파일의 S3 URL 반환
        return s3Client.getUrl(AWS_S3_BUKET_NAME, request.getKey()).toString();
    }


    public PutObjectRequest createPutObjectRequest(AudioInfo audioInfo) {
        try {
            return new PutObjectRequest(
                    AWS_S3_BUKET_NAME,
                    generateFileKey("concat/result", audioInfo.getFileName()),
                    audioInfo.toInputStream(),
                    createObjectMetadata(audioInfo)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("입력 파라미터에 문제가 있습니다. 파일 업로드 불가!", e);
        }
    }

    private ObjectMetadata createObjectMetadata(AudioInfo audioInfo) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(audioInfo.getContentType());
        objectMetadata.setContentLength(audioInfo.getContentSize());
        return objectMetadata;
    }

    public static AudioInputStream load(String s3Url) {
        LOGGER.log(Level.INFO,"[S3Service] s3Url 값 확인 : " + s3Url);
        try {
            URL url = new URL(s3Url);
            LOGGER.log(Level.INFO,"[S3Service] url 값 확인 : " + url);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            LOGGER.log(Level.INFO,"[S3Service] audioInputStream 값 확인 : " + audioInputStream);

            byte[] bytes = AudioExtensionConverter.mp3ToWav(audioInputStream);
            LOGGER.log(Level.INFO,"[S3Service] bytes 값 확인 : " + bytes.length);

            return AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("잘못된 URL입니다");
        } catch (IOException e) {
            throw new IllegalArgumentException("오디오 파일이 아닙니다.");
        } catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("지원하지 않는 오디오 형식입니다");
        }
    }
}




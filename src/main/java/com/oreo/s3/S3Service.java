package com.oreo.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.oreo.concat.concatenator.model.AudioInfo;
import com.oreo.s3.config.S3Config;

import java.util.UUID;

public class S3Service {

    private static final String AWS_S3_BUKET_NAME = System.getenv("AWS_S3_BUKET_NAME");
    private final AmazonS3 s3Client;
    public S3Service() {
        s3Client = S3Config.amazonS3Client();
    }

//    public List<String> upload(List<MultipartFile> files, String dirName) {
//        if (files.isEmpty()) {
//            throw new IllegalArgumentException("파일이 없습니다");
//        }
//
//        List<String> uploadedUrls = new ArrayList<>();
//        for (MultipartFile file : files) {
//            uploadedUrls.add(uploadSingleFile(file, dirName));
//        }
//        return uploadedUrls;
//    }
//
//    // 공통 업로드 로직을 처리하는 메서드
//    public String uploadSingleFile(MultipartFile file, String dirName) {
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("파일이 없습니다");
//        }
//
//        // 업로드할 객체의 메타데이터 정보 추출 및 ObjectMetadata 초기화
//        ObjectMetadata objectMetadata = createObjectMetadata(file);
//
//        // 버킷 내 저장 경로(key) 설정
//        String key = generateFileKey(dirName, file.getOriginalFilename());
//
//        // 객체 추가 요청 정보 초기화
//        PutObjectRequest request = createPutObjectRequest(file, key, objectMetadata);
//
//        // S3 버킷에 객체 추가
//        s3Client.putObject(request);
//
//        // 업로드한 파일의 S3 URL 반환
//        return s3Client.getUrl(buketName, key).toString();
//    }
//
//    // 파일의 메타데이터 생성
//    private ObjectMetadata createObjectMetadata(MultipartFile file) {
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(file.getContentType());
//        objectMetadata.setContentLength(file.getSize());
//        return objectMetadata;
//    }
//
    // 파일의 키 생성
    private String generateFileKey(String dirName, String originalFilename) {
        return dirName + "/" + UUID.randomUUID() + "_" + originalFilename;
    }
//
//    // PutObjectRequest 생성
//    private PutObjectRequest createPutObjectRequest(MultipartFile file, String key, ObjectMetadata objectMetadata) {
//        try {
//            return new PutObjectRequest(
//                    AWS_S3_BUKET_NAME,
//                    key,
//                    file.getInputStream(),
//                    objectMetadata
//            );
//        } catch (IOException e) {
//            throw new IllegalArgumentException("입력 파라미터에 문제가 있습니다. 파일 업로드 불가!", e);
//        }
//    }

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

    public String uploadSingleFile(AudioInfo info, String dirName) {
        if (info == null) {
            throw new IllegalArgumentException("파일이 없습니다");
        }

        // 키 정보 생성
        String key = generateFileKey(dirName, info.getFileName());

        // 객체 추가 요청 정보 초기화
        PutObjectRequest request = creatPutObjectRequest(info, key);

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

    public PutObjectRequest creatPutObjectRequest(AudioInfo audioInfo, String key) {
        try {
            return new PutObjectRequest(
                    AWS_S3_BUKET_NAME,
                    key,
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
}




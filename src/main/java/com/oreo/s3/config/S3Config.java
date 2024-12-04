package com.oreo.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;

public class S3Config {
    private static final String AWS_S3_ACCESSKEY = System.getenv("AWS_S3_ACCESSKEY");
    private static final String AWS_S3_REGION = System.getenv("AWS_S3_REGION");
    private static final String AWS_S3_SECRETKEY = System.getenv("AWS_S3_SECRETKEY");
    public static AmazonS3 amazonS3Client() {
        // AWS 자격 증명 객체 생성
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(AWS_S3_ACCESSKEY, AWS_S3_SECRETKEY);

        // AmazonS3 객체 생성 및 반환
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(AWS_S3_REGION)) // 리전 설정
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)) // 자격 증명 등록
                .build();
    }

}

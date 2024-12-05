package com.oreo.vc;


import com.oreo.concat.concatenator.model.AudioInfo;
import com.oreo.s3.S3Service;
import com.oreo.util.AudioPlayBackLength;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VcAPIResult {
    private static final Logger LOGGER = Logger.getLogger("com.oreo.vc.VcApiServiceTest");
    private static final VcAPIService vc = new VcAPIService();
    private static final S3Service s3 = new S3Service();

    /**
     * TRG ID 추출
     * @param url
     * @return
     */
    public static String trg(String url) {
        AudioInputStream s3file = S3Service.load(url);
        LOGGER.log(Level.INFO, "[vcServiceTest] s3file 요청값 확인 : " + s3file);

        AudioInfo a = new AudioInfo(s3file, "trgfile.wav",s3file.getFrameLength(), "audio/wav");
        LOGGER.log(Level.INFO, "[vcServiceTest] a 요청값 확인 : " + a);

        String s = vc.trgIdCreate(a);
        LOGGER.log(Level.INFO, s);

        return s;
    }

    /**
     * Src URl과 TrgId 가지고 결과 Dto 생성
     * @param urls
     * @param trgId
     */
    public static List<VcResultDto> result(List<String> urls, String trgId) throws IOException, UnsupportedAudioFileException {
        List<AudioInfo> audioInfos = new ArrayList<>();
        for (String url : urls){
            AudioInputStream srcfile = S3Service.load(url);
            LOGGER.log(Level.INFO, "[vcServiceTest] srcfile 요청값 확인 : " + srcfile);

            AudioInfo src= new AudioInfo(srcfile, "srcfile.wav", srcfile.getFrameLength(), "audio/wav");
            LOGGER.log(Level.INFO, "[vcServiceTest] src 요청값 확인 : " + src);
            audioInfos.add(src);
        }
        LOGGER.log(Level.INFO, "[vcServiceTest] audioInfos 값 확인 : " + audioInfos);

        List<AudioInfo> audios = vc.resultFileCreate(audioInfos, trgId);
        LOGGER.log(Level.INFO, "[vcServiceTest] audios 요청값 확인 : " + audios.toString());
//        여기서 파일 데이터 추출 name, size, length, extension 에다가 밑에 update 까지 합처서 리턴
        List<String> upload = s3.upload(audios, "vc/result");
        LOGGER.log(Level.INFO, "[vcServiceTest] upload 요청값 확인 : " + upload.toString());
        List<VcResultDto> vcResultDtos = new ArrayList<>();
        for (int i = 0; i < audios.size(); i++) {
            VcResultDto vcResultDto = new VcResultDto(
                    audios.get(i).getFileName(),
                    String.valueOf(audios.get(i).getContentSize()),
                    AudioPlayBackLength.calculateTargetFrames(AudioSystem.getAudioInputStream(audios.get(i).toInputStream())),
                    getFileExtension(audios.get(i).getFileName()),
                    upload.get(i));
            vcResultDtos.add(vcResultDto);
        }
        LOGGER.log(Level.INFO,"vcResultDtos.toString() = " + vcResultDtos.toString());
        return vcResultDtos;
    }


    /**
     * 파일 확장자 추출
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 null이거나 비어 있습니다.");
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("유효하지 않은 파일 이름입니다: 확장자를 찾을 수 없습니다.");
        }

        return fileName.substring(lastDotIndex + 1);
    }


}
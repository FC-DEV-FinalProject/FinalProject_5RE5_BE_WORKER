package com.oreo.concat.concatenator.model;

import com.oreo.concat.concatenator.dto.AudioProperties;
import com.oreo.concat.concatenator.audio.AudioExtensionConverter;
import com.oreo.concat.concatenator.audio.AudioResample;
import com.oreo.concat.concatenator.dto.ConcatRowRequest;
import com.oreo.concat.concatenator.dto.OriginAudioRequest;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LimitFileLoader {
    private int index = 0;

    public List<AudioProperties> loadFile(List<ConcatRowRequest> audios, AudioResample audioResample) {
        long bytes = 0;
        List<AudioProperties> audioProperties = new ArrayList<>();
        while (index < audios.size()) {
            ConcatRowRequest concatRowRequest = audios.get(index);
            if (bytes <= 350 * 1024 * 1024) {//350MB 제한
                OriginAudioRequest originAudioRequest = concatRowRequest.getOriginAudioRequest();
                AudioInputStream load = audioResample.resample(load(originAudioRequest.getAudioUrl()));

                audioProperties.add(new AudioProperties(load, concatRowRequest.getRowSilence()));
                bytes += originAudioRequest.getFileSize();
                index++;
                continue;
            }
            break;
        }
        return audioProperties;
    }

    public void resetIndex() {
        this.index = 0; // 인덱스 초기화
    }

    public static AudioInputStream load(String s3Url)  {
        try {
            URL url = new URL(s3Url);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            byte[] bytes = AudioExtensionConverter.mp3ToWav(audioInputStream);

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
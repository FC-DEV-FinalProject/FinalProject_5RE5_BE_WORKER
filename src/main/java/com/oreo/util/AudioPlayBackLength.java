package com.oreo.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

public class AudioPlayBackLength {
    /**
     * 오디오 포멧을 가지고 프레임 사이즈로 재생길이를 출력 시키는 메서드
     * @param audioStream
     * @return
     * @throws IOException
     */
    public static int calculateTargetFrames(AudioInputStream audioStream) throws IOException {
        AudioFormat format = audioStream.getFormat();
        int frameSize = format.getFrameSize();
        byte[] buffer = new byte[4096];
        long totalBytes = 0;
        int bytesRead;
        float frameRate = format.getFrameRate();

        while ((bytesRead = audioStream.read(buffer)) != -1) {
            totalBytes += bytesRead;
        }

        Long calculatedFrames = totalBytes / frameSize;
        double totalSeconds = calculatedFrames / frameRate;
        return (int) totalSeconds;
    }
}

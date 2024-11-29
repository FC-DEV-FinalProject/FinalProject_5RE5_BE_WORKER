package com.oreo.concat.concatenator;


import com.oreo.concat.concatenator.audio.AudioExtensionConverter;
import com.oreo.concat.concatenator.audio.AudioResample;
import com.oreo.concat.concatenator.audio.BeepMaker;
import com.oreo.concat.concatenator.dto.AudioProperties;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.oreo.concat.concatenator.IntervalConcatenator.prepareAudioStreams;

/**
 * @apiNote 모노포맷의 무음구간을 포함한 병합에 사용되는 클래스 입니다. <br>
 * 오디오 파일은 병합 하려는 포맷 타입과 같거나 같아지도록 리샘플링 되어야 합니다. <br>
 * WAVE 파일만을 지원하며 mp3파일의 경우 {@link AudioExtensionConverter} 를 사용하여 WAVE 파일로 변환합니다.
 * @see AudioResample
 * @see AudioExtensionConverter
 */
public class MonoIntervalConcatenator extends MonoConcatenator implements IntervalConcatenator {
    public final AudioFormat AUDIO_FORMAT;

    public MonoIntervalConcatenator(AudioFormat audioFormat) {
        this.AUDIO_FORMAT = audioFormat;
    }

    /**
     * @apiNote 오디오 스트림과 무음 오디오 스트림을 하나의 바이트 배열로 만들어 반환합니다.
     * @param audioStreams
     * @param start
     * @return
     * @throws IOException
     */
    @Override
    public ByteArrayOutputStream intervalConcatenate(List<AudioProperties> audioStreams, float start) {
        List<AudioInputStream> list = prepareAudioStreams(audioStreams, AUDIO_FORMAT);
        list.add(0, BeepMaker.makeSound(start * 1000, AUDIO_FORMAT));
        try {
            return super.concatenate(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AudioFormat getFormat() {
        return AUDIO_FORMAT;
    }
}
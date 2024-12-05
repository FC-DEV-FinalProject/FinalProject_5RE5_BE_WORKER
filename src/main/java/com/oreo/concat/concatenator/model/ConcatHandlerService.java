package com.oreo.concat.concatenator.model;

import com.oreo.concat.concatenator.dto.AudioProperties;
import com.oreo.concat.concatenator.IntervalConcatenator;
import com.oreo.concat.concatenator.audio.AudioResample;
import com.oreo.concat.concatenator.dto.ConcatRowRequest;

import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ConcatHandlerService {

    private final IntervalConcatenator concatenator;
    private final LimitFileLoader limitFileLoader;
    private final AudioResample audioResample;
    public ConcatHandlerService(IntervalConcatenator concatenator) {
        this.concatenator = concatenator;
        this.limitFileLoader = new LimitFileLoader();
        this.audioResample = new AudioResample(concatenator.getFormat());
    }

    public List<AudioProperties> loadAudios(List<ConcatRowRequest> audios) {
        return limitFileLoader.loadFile(audios, audioResample);
    }

    public ByteArrayOutputStream concat(List<AudioProperties> audios, float start) {
        return concatenator.intervalConcatenate(audios, start);
    }


    public AudioInputStream resample(ByteArrayOutputStream byteArrayOutputStream) {
        return audioResample.resample(byteArrayOutputStream);
    }
}

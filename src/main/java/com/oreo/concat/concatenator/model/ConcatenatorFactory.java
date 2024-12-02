package com.oreo.concat.concatenator.model;

import com.oreo.concat.concatenator.IntervalConcatenator;
import com.oreo.concat.concatenator.MonoIntervalConcatenator;
import com.oreo.concat.concatenator.StereoIntervalConcatenator;
import com.oreo.concat.concatenator.audio.AudioFormats;

import javax.sound.sampled.AudioFormat;

public class ConcatenatorFactory {

    private ConcatenatorFactory() {}

    public static IntervalConcatenator of(AudioFormat audioFormat) {
        int channels = audioFormat.getChannels();
        if (channels == 2) {
            return new StereoIntervalConcatenator(audioFormat);
        }

        if (channels == 1) {
            return new MonoIntervalConcatenator(audioFormat);
        }
        return new StereoIntervalConcatenator(AudioFormats.STEREO_FORMAT_SR441_B16);
    }
}

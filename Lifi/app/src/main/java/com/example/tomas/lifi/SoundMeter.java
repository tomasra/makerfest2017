package com.example.tomas.lifi;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;


public class SoundMeter {

    private AudioRecord _ar = null;
    private int minSize;

    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };

    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d("", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("", rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        Log.e("", "No configuration was valid!!!");
        return null;
    }




    public void start() {
        try {
                _ar = findAudioRecord();

                minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
                _ar.startRecording();
            }
        catch(Exception e){
            Log.e("TrackingFlow", "Exception", e);
        }
    }

    public void stop() {
        if (_ar != null) {
            _ar.stop();
            _ar.release();
            _ar = null;
        }
    }

    public double getAmplitude() {
        short[] buffer = new short[minSize];
        _ar.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer)
        {
            if (Math.abs(s) > max)
            {
                max = Math.abs(s);
            }
        }
        return max;
    }

}
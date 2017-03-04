package com.example.tomas.lifi;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;






public class SoundMeter {

    private AudioRecord ar = null;
    private int minSize;

    public int getValidSampleRate() {
        for (int rate : new int[] {8000, 11025, 16000, 22050, 44100}) {  // add the rates you wish to check against
            int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_CONFIGURATION_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                // buffer size is valid, Sample rate supported
                Log.i("Rate supported:", rate + " ");
                return rate;
            }
        }
        return -1;
    }

    public void start(int sampleRate) {
        try {

            minSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            ar = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
            ar.release();
            if (ar.getState() == AudioRecord.STATE_INITIALIZED){
                ar.startRecording();
            }
            else {
                Log.e("ar.getState()", "Not initialised");
            }

        }
        catch(Exception e){
            Log.e("TrackingFlow", "Exception", e);
        }
    }

    public void stop() {
        if (ar != null) {
            ar.stop();
        }
    }

    public double getAmplitude() {
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);
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
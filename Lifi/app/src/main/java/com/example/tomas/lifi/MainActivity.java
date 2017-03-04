package com.example.tomas.lifi;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

class recorder extends Activity  {

    private Thread thread;
    private boolean isRecording;
    private AudioRecord recorder;
    private FileOutputStream os;
    private BufferedOutputStream bos;
    private DataOutputStream dos;
    private TextView text;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRate = 8000;
    private int channel = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int encoding = AudioFormat.ENCODING_PCM_16BIT;
    private int result = 0;
    private int bufferSize;
    private byte[] buffer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("onCreate", "layout set, about to init audiorec obj");
        text = (TextView)findViewById(R.id.amplitude);

        bufferSize = AudioRecord.getMinBufferSize(sampleRate,channel,encoding);
        buffer = new byte[bufferSize];

        recorder = new AudioRecord(audioSource, sampleRate,channel,encoding,
                AudioRecord.getMinBufferSize(sampleRate, channel,encoding));
        Log.i("recorder obj state",""+recorder.getRecordingState());
    }

    public void onClickPlay(View v){

    }


    public void record(){
        Log.i("inside record method", "******");


        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,channel,encoding);
        byte[] buffer = new byte[bufferSize];


            recorder.startRecording();

            isRecording = true;
            try {
                while (isRecording) {
                    result = recorder.read(buffer, 0, bufferSize);
                    for (int a = 0; a < result; a++) {
                        dos.write(buffer[a]);

                        if (!isRecording) {
                            recorder.stop();
                            break;
                        }

                    }

                }
                dos.flush();
                dos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }// end of record method

    public void onClickStop(View v){
        Log.v("onClickStop", "stop clicked");
        isRecording=false;
    }
    public void onClickReverse(View v){
        Log.v("onClickReverse", "reverse clicked");
    }
    public void onClickRecord(View v){
        Log.v("onClickRecourd", "record clicked, thread gona start");
        text.setText("recording");
        thread = new Thread(new Runnable() {
            public void run() {
                isRecording = true;
                record();
            }
        });

        thread.start();
        isRecording = false;
    }
}//end of class

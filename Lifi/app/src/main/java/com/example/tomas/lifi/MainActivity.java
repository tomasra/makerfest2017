package com.example.tomas.lifi;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends Activity  {

    private Thread thread;
    private boolean isRecording;

    private TextView textTxt;

    private SoundMeter _soundMeter;

    private long _timer;
    private LineGraphSeries<DataPoint> _series;
    private GraphView _graph;


    private final Handler _handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTxt = (TextView)findViewById(R.id.amplitude);
        _soundMeter = new SoundMeter();
        _graph = (GraphView) findViewById(R.id.graph);
        _series = new LineGraphSeries<DataPoint>();
        _graph.addSeries(_series);
//        _graph.getViewport().setYAxisBoundsManual(true);
//        _graph.getViewport().setMinY(30000);
//        _graph.getViewport().setMaxY(40000);

        _timer = 4;

       }


    public void record(){
        Log.i("inside record method", "******");
        try {
            isRecording = true;
            while (isRecording) {
                double result = _soundMeter.getAmplitude();
                setText(textTxt, result + "", result, _graph);
                 if (!isRecording) {
                    _soundMeter.stop();
                    break;
                }
             }
                _soundMeter.stop();

            } catch (Exception e) {
                e.printStackTrace();
            }

    }// end of record method

    public void onClickStop(View v){
        Log.v("onClickStop", "stop clicked");
        isRecording=false;
    }

    private void setText(final TextView text,final String value, final double doubleValue, final GraphView graph){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
                _series.appendData( new DataPoint(_timer++, doubleValue),true,30);
                graph.removeAllSeries();
                graph.addSeries(_series);
                //_handler.postDelayed(this, 200);
                //_graph.refreshDrawableState();
            }
        });
        }
    public void onClickRecord(View v){
        Log.v("onClickRecourd", "record clicked, thread gona start");
        textTxt.setText("recording");
        thread = new Thread(){
            @Override
            public void run() {
                isRecording = true;
                _soundMeter.start();
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.soundwave1);
                mp.setLooping(true);
                mp.start();
                record();
            };
        };
        thread.start();
    }
}//end of class

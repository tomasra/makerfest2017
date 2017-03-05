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
    private MediaPlayer _mp;

    private final Handler _handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTxt = (TextView)findViewById(R.id.periodasTxtBox);
        _soundMeter = new SoundMeter();
        _graph = (GraphView) findViewById(R.id.graph);
        _series = new LineGraphSeries<DataPoint>();
        _graph.addSeries(_series);
//        _graph.getViewport().setYAxisBoundsManual(true);
//        _graph.getViewport().setMinY(-33000);
//        _graph.getViewport().setMaxY(33000);

        _timer = 4;

        _mp = MediaPlayer.create(getApplicationContext(), R.raw.soundwave1);
        _mp.setLooping(true);
        //_mp.setVolume(1,1);
    }


    public void record(){
        Log.i("inside record method", "******");
        try {
            isRecording = true;
            while (isRecording) {
                //double result = _soundMeter.getAmplitude();
                short[] buffer = _soundMeter.getBuffer();
                //setText(textTxt, result + "", result, _graph);
                setText(textTxt, buffer[0] + "",  _graph, buffer);
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

    private void setText(final TextView text,final String value, final GraphView graph, final short[]  buffer){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
                _timer = 0;
                DataPoint[] dataPoints = new DataPoint[buffer.length];
                for(int i = 0; i < buffer.length; i++){ //buffer.length
                    dataPoints[i] = new DataPoint(_timer++, buffer[i]);
                    //_series.appendData( new DataPoint(_timer++, buffer[i]),true,200);

                }
                _series.resetData(dataPoints);
                //_series.appendData( new DataPoint(_timer++, doubleValue),true,30);
                graph.removeAllSeries();
                graph.addSeries(_series);
                //_mp.setVolume(1,1);
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

                        _mp.start();
                        record();
            };
        };
        thread.start();
    }
}//end of class

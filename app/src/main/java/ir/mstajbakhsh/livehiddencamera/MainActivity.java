package ir.mstajbakhsh.livehiddencamera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.faucamp.simplertmp.RtmpHandler;

import java.io.IOException;
import java.net.SocketException;

import ir.mstajbakhsh.livehiddencamera.HiddenCameraLayout.CameraConfig;
import ir.mstajbakhsh.livehiddencamera.HiddenCameraLayout.CameraFacing;
import ir.mstajbakhsh.livehiddencamera.HiddenCameraLayout.HiddenCameraLayout;
import ir.mstajbakhsh.livehiddencamera.LiveBroadcaster.SrsCameraView;
import ir.mstajbakhsh.livehiddencamera.LiveBroadcaster.SrsEncodeHandler;
import ir.mstajbakhsh.livehiddencamera.LiveBroadcaster.SrsPublisher;
import ir.mstajbakhsh.livehiddencamera.LiveBroadcaster.SrsRecordHandler;

public class MainActivity extends AppCompatActivity implements RtmpHandler.RtmpListener, SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {

    Button btnPusblish;
    EditText txtURL;
    String rtmpURL = "rtmp://IP/live";

    private SrsPublisher mPublisher;
    private SrsCameraView mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPusblish = findViewById(R.id.btnRtmpHandler);
        btnPusblish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rtmpURL = txtURL.getText().toString();

                initHiddenCam();
                finish();
            }
        });

        txtURL = findViewById(R.id.txtURL);
    }

    private void initHiddenCam() {
        HiddenCameraLayout l = new HiddenCameraLayout(getApplicationContext(), new HiddenCameraLayout.PermissionHandler() {
            @Override
            public void onPermissionNotGrantedException(Exception ex) {
                Log.d("HCL", "Ask user to grant permission.");
                Log.e("HCL", ex.getMessage());
            }
        });

        //start config
        CameraConfig cameraConfig = new CameraConfig()
                .getBuilder(MainActivity.this.getApplicationContext())
                .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                .build();


        mCameraView = l.initHiddenLayout(cameraConfig);

        mPublisher = new SrsPublisher(mCameraView);
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        mPublisher.setPreviewResolution(640, 360);
        mPublisher.setOutputResolution(640, 360);
        mPublisher.setVideoHDMode();
        mPublisher.startPublish(rtmpURL);
        mPublisher.startCamera();
    }

    @Override
    public void onRtmpConnecting(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpConnected(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {
        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpDisconnected() {
        Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    @Override
    public void onNetworkWeak() {
        Toast.makeText(getApplicationContext(), "Network weak", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkResume() {
        Toast.makeText(getApplicationContext(), "Network resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRecordPause() {

    }

    @Override
    public void onRecordResume() {

    }

    @Override
    public void onRecordStarted(String msg) {

    }

    @Override
    public void onRecordFinished(String msg) {

    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    private void handleException(Exception e) {
        try {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            mPublisher.stopPublish();
            mPublisher.stopRecord();
        } catch (Exception e1) {

        }
    }
}

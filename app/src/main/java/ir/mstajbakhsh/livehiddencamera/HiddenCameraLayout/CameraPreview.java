package ir.mstajbakhsh.livehiddencamera.HiddenCameraLayout;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private CameraConfig mCameraConfig;

    private volatile boolean safeToTakePicture = false;

    public CameraPreview(@NonNull Context context) {
        super(context);

        //Set surface holder
        if (getHolder() == null) {
            initSurfaceView();
        }
    }

    /**
     * Initialize the surface view holder.
     */
    private void initSurfaceView() {
        mHolder = super.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //Do nothing
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //Do nothing
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mCamera == null) {  //Camera is not initialized yet.
            Log.e("HiddenCam", "Error in openning camera.");
            return;
        } else if (surfaceHolder.getSurface() == null) { //Surface preview is not initialized yet
            Log.e("HiddenCam", "Error in openning camera.");
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore: tried to stop a non-existent preview
        }

        requestLayout();

        //Optional Params can be set here.
        //Camera.Parameters parameters = mCamera.getParameters();
        //parameters.setPreviewFormat(ImageFormat.NV21);
        //parameters.setPreviewFpsRange(4000,60000);
        //parameters.setPreviewSize(640, 360);
        //mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();

            safeToTakePicture = true;
        } catch (IOException | NullPointerException e) {
            //Cannot start preview
            Log.e("HiddenCam", "Error in openning camera.");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Call stopPreview() to stop updating the preview surface.
        if (mCamera != null) mCamera.stopPreview();
    }

    /**
     * Initialize the camera and start the preview of the camera.
     *
     * @param cameraConfig camera config builder.
     */
    public void startCameraInternal(@NonNull CameraConfig cameraConfig) {
        mCameraConfig = cameraConfig;

        if (safeCameraOpen(mCameraConfig.getFacing())) {
            if (mCamera != null) {
                requestLayout();

                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("HiddenCam", "Error in openning camera.");
                }
            }
        } else {
            Log.e("HiddenCam", "Error in openning camera.");
        }
    }

    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            stopPreviewAndFreeCamera();

            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e("CameraPreview", "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    boolean isSafeToTakePictureInternal() {
        return safeToTakePicture;
    }

    /**
     * When this function returns, mCamera will be null.
     */
    void stopPreviewAndFreeCamera() {
        safeToTakePicture = false;
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public SurfaceHolder getHolder() {
        if (mHolder == null) {
            initSurfaceView();
        }

        return mHolder;
    }
}

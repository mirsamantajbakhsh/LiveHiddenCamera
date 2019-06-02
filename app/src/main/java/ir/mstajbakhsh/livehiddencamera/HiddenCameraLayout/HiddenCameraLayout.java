package ir.mstajbakhsh.livehiddencamera.HiddenCameraLayout;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;

import ir.mstajbakhsh.livehiddencamera.LiveBroadcaster.SrsCameraView;

public class HiddenCameraLayout {
    Context cntx;
    SrsCameraView cam;
    WindowManager mWindowManager;
    PermissionHandler ph;

    public HiddenCameraLayout(Context cntx, PermissionHandler ph) {
        this.cntx = cntx;
        this.ph = ph;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public SrsCameraView initHiddenLayout(CameraConfig config) {
        cam = new SrsCameraView(getContext());

        cam.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        try {
            mWindowManager.addView(cam, params);
        } catch (Exception ex) {
            ph.onPermissionNotGrantedException(ex);
        }

        cam.startCameraInternal(config);
        return cam;
    }


    private Context getContext() {
        return cntx;
    }

    public interface PermissionHandler {
        void onPermissionNotGrantedException(Exception ex);
    }
}

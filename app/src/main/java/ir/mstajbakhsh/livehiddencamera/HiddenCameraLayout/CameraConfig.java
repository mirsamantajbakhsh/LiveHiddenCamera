package ir.mstajbakhsh.livehiddencamera.HiddenCameraLayout;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

public class CameraConfig {
    private Context mContext;

    @CameraFacing.SupportedCameraFacing
    private int mFacing = CameraFacing.REAR_FACING_CAMERA;

    private File mImageFile;

    public CameraConfig() {
        // Do nothing
    }

    public Builder getBuilder(Context context) {
        mContext = context;
        return new Builder();
    }


    @CameraFacing.SupportedCameraFacing
    int getFacing() {
        return mFacing;
    }


    public class Builder {
        /**
         * Set the camera facing with which you want to capture image.
         * Either rear facing camera or front facing camera. If you don't provide any camera facing,
         * default camera facing will be {@link CameraFacing#FRONT_FACING_CAMERA}.
         *
         * @param cameraFacing Any camera facing from:
         *                     <li>{@link CameraFacing#REAR_FACING_CAMERA}</li>
         *                     <li>{@link CameraFacing#FRONT_FACING_CAMERA}</li>
         * @return {@link Builder}
         * @see CameraFacing
         */
        public Builder setCameraFacing(@CameraFacing.SupportedCameraFacing int cameraFacing) {
            //Validate input
            if (cameraFacing != CameraFacing.REAR_FACING_CAMERA &&
                    cameraFacing != CameraFacing.FRONT_FACING_CAMERA) {
                throw new RuntimeException("Invalid camera facing value.");
            }

            mFacing = cameraFacing;
            return this;
        }



        /**
         * Set the location of the out put image. If you do not set any file for the output image, by
         * default image will be stored in the application's cache directory.
         *
         * @param imageFile {@link File} where you want to store the image.
         * @return {@link Builder}
         */
        public Builder setImageFile(File imageFile) {
            mImageFile = imageFile;
            return this;
        }

        /**
         * Build the configuration.
         *
         * @return {@link CameraConfig}
         */
        public CameraConfig build() {
            if (mImageFile == null) mImageFile = getDefaultStorageFile();
            return CameraConfig.this;
        }

        /**
         * Get the new file to store the image if there isn't any custom file location available.
         * This will create new file into the cache directory of the application.
         */
        @NonNull
        private File getDefaultStorageFile() {
            return new File("/sdcard/DCIM/test.jpg");
        }
    }
}

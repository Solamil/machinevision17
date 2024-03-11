package cz.vision.machinevision2017.vision;



import com.squareup.otto.Bus;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import cz.vision.machinevision2017.MainActivity;
import cz.vision.machinevision2017.activities.BusProvider;
import cz.vision.machinevision2017.thread.VisionThread;
import cz.vision.machinevision2017.vision.Vision;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.ObjectInfo;
import cz.vision.mylog.MyLog;


public class PreprocessingImage {
    private static final long TIME_BETWEEN_FRAMES = 150;
    private static final long TIME_BETWEEN_HIGHLIGHTED = 2000;
    private VisionThread vision;
    private Thread workerThread;
    private long lastFrameTime;
    private BlockingDeque<Mat> rgbaDeque;
    private Vision machineVision;
    private long lastHighlightTime;
    private Rect rect;
    private ObjectInfo objectInfo;
    public PreprocessingImage() {
        rgbaDeque = new LinkedBlockingDeque<>();
        vision = new VisionThread(rgbaDeque);
//        vision = new VisionThread(rgbaArray);
        rect = null;
        workerThread = new Thread(vision);
        workerThread.start();
        lastFrameTime = System.currentTimeMillis();
        lastHighlightTime = System.currentTimeMillis();
    }

    public void  setCameraScreen(Mat src) {
        long currentTime = System.currentTimeMillis();
//        runOnUIthread(src);
        if (currentTime - this.lastFrameTime > TIME_BETWEEN_FRAMES) {
            thread(src);


            this.lastFrameTime = System.currentTimeMillis();

        }
        if (currentTime - this.lastHighlightTime < TIME_BETWEEN_HIGHLIGHTED) {
            if (rect != null) {
                highlightObject(src);
            }
        } else {
            if (vision.getObjectData() != null) {

                if (vision.getObjectData().getObjectRect() != null) {
                    rect = vision.getObjectData().getObjectRect();
                    objectInfo = vision.getObjectData();
                    Bus bus = BusProvider.getInstance();
                    bus.register(this);
                    bus.post(new ObjectInfo(objectInfo.getObjectRect(), objectInfo.getName()));
                    vision.getObjectData().setObjectRect(null);
                    this.lastHighlightTime = System.currentTimeMillis();

                }
            }
        }

    }
    public void stateThread(boolean isRunning) {
        vision.stateThread(isRunning);
    }

    private void highlightObject(Mat src) {
        if (rect != null) {
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }
    }

    private void thread(Mat src) {
        try {
            rgbaDeque.putLast(src);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runOnUIthread(Mat src) {
        if (machineVision == null) machineVision = new Vision();

        machineVision.cameraScreen(src);
    }
}

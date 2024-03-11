package cz.vision.machinevision2017.thread;

import android.os.Looper;

import org.opencv.core.Mat;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import cz.vision.machinevision2017.activities.Singleton;
import cz.vision.machinevision2017.vision.Vision;
import cz.vision.machinevision2017.vision.imgprocess.Image;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.ObjectInfo;
import cz.vision.mylog.MyLog;

/**
 * Created by Michal on 16.11.2017.
 */

public class VisionThread implements Runnable {
    private boolean isRunning;
    private BlockingDeque<Mat> rgbaDeque;
    private BlockingQueue<Mat> rgbaArray;
    private Vision machineVision;
    private static final long TIMEOUT = 1000;
    public VisionThread(BlockingDeque<Mat> rgbaDeque){
        isRunning = true;
        this.rgbaDeque = rgbaDeque;
        machineVision = new Vision();
    }
    public VisionThread(BlockingQueue<Mat> rgbaArray){
        isRunning = true;
        this.rgbaArray =rgbaArray;
        machineVision = new Vision();
    }
    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        while(true){
            if(!isRunning){

                continue; //sleeping
            }
            //TODO
            try {
                Mat srcQueue =  rgbaDeque.pollLast(TIMEOUT, TimeUnit.MILLISECONDS);
                if(srcQueue != null){
                    machineVision.cameraScreen(srcQueue);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void stateThread(boolean isRunning){
        this.isRunning = isRunning;
    }
    public ObjectInfo getObjectData(){
        return machineVision.getObjectData();
    }
}

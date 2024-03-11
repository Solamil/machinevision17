package cz.vision.machinevision2017.vision.recognitionobject;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;

import cz.vision.machinevision2017.vision.imgprocess.Color;
import cz.vision.machinevision2017.vision.imgprocess.Image;
import cz.vision.machinevision2017.vision.recognitionobject.classificatorI.DetectObjectListener;
import cz.vision.machinevision2017.vision.recognitionobject.classifierxml.FilePathClassifier;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.ObjectInfo;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.filter.VisionFilter;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.object.Light;
import cz.vision.mylog.MyLog;

/**
 * Created by Michal on 16.11.2017.
 */

public class RecognitionDetection {
    private ArrayList<Light> lightList;
    private double confidence;
    private String name;
    private VisionFilter filter;
    private ObjectInfo objectData;

    public RecognitionDetection() {
        filter = new VisionFilter(detectedObject);
        lightList = new ArrayList<>();
        lightList.add(new Light(FilePathClassifier.BATTERY, "BATTERY", Color.RED));
        lightList.add(new Light(FilePathClassifier.TIRE, "TIRE", Color.YELLOW));
        lightList.add(new Light(FilePathClassifier.ECU, "ECU", Color.YELLOW));
        lightList.add(new Light(FilePathClassifier.TEMP, "TEMP", Color.RED));
        lightList.add(new Light(FilePathClassifier.TRACTION, "TRACT", Color.YELLOW));
        objectData = null;
        confidence = -100;
    }
    private DetectObjectListener detectedObject = new DetectObjectListener() {
        @Override
        public void detectObject(Mat object, Color color, Rect rect) {
            if(RecognitionDetection.this.recognizeObject(object, color)){
                objectData = new ObjectInfo(rect, name);

            }
        }
    };
    public void detectObject(Mat src){
        filter.applyScreen(src);
    }
    private boolean recognizeObject(Mat object, Color color) {
//        Image.takePhoto(object);
        for (Light light : lightList) {
            if (color == light.getColor()) {
                light.detectObject(object);
                if (confidence < light.getConfindence()) {
                    confidence = light.getConfindence();
                }
            }
        }

        for (Light light : lightList) {
            if (confidence == light.getConfindence() && confidence != -100 && confidence >= 0) {
                name = light.getName();
//                MyLog.i(MyLog.TAG, light.getName() + light.getConfindence());

//                    Image.takePhoto(object);
//                    MyLog.i(MyLog.TAG, light.getName());
            }
        }
        for (Light light : lightList) {
            if (light.getConfindence() >= 0) {
                light.setConfindence(-100);
            }
        }
        if (confidence >= 0) {
            confidence = -100;
            return true;
        }
        return false;
    }
    public ObjectInfo getObjectData(){
        return objectData;
    }
}

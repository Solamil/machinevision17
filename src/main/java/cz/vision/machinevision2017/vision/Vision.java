package cz.vision.machinevision2017.vision;

import org.opencv.core.Mat;

import cz.vision.machinevision2017.vision.recognitionobject.RecognitionDetection;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.ObjectInfo;

/**
 * Created by Michal on 16.11.2017.
 */

public class Vision {
    private RecognitionDetection recogDetect;
    public Vision(){
        recogDetect = new RecognitionDetection();
    }
    public void cameraScreen(Mat src){

        recogDetect.detectObject(src);
    }
    public ObjectInfo getObjectData(){
        return recogDetect.getObjectData();
    }
}

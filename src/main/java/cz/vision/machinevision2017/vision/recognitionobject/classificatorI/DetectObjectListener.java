package cz.vision.machinevision2017.vision.recognitionobject.classificatorI;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import cz.vision.machinevision2017.vision.imgprocess.Color;

/**
 * Created by Michal on 16.11.2017.
 */

public interface DetectObjectListener {
    void detectObject(Mat object, Color color, Rect rect);
}

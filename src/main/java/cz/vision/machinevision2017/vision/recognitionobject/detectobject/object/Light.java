package cz.vision.machinevision2017.vision.recognitionobject.detectobject.object;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

import cz.vision.machinevision2017.vision.imgprocess.Color;
import cz.vision.machinevision2017.vision.recognitionobject.classifierxml.FilePathClassifier;
import cz.vision.mylog.MyLog;

public class Light {
    private CascadeClassifier classifier;
    private String nameLight;
    private Color light;
    private MatOfRect matOfRect;
    private MatOfInt rejectLevels;
    private MatOfDouble levelWeights;
    private double confindence;

    public Light(String pathXML, String nameLight, Color light) {
        classifier = new CascadeClassifier();
        this.nameLight = nameLight;
        this.light = light;
        matOfRect = new MatOfRect();
        levelWeights = new MatOfDouble();
        rejectLevels = new MatOfInt();
        this.nameLight = nameLight;
        this.light = light;
        confindence = -100;

        if (!classifier.load(pathXML)) {
            FilePathClassifier.failedToLoad(pathXML);
        }
    }

    public void detectObject(Mat object) {

        classifier.detectMultiScale3(object, matOfRect, rejectLevels, levelWeights, 1.4112, 0, 1, object.size(), object.size(), true);
        rejectLevels.release();

        matOfRect.release();
        if (!levelWeights.empty()) {
            confindence = levelWeights.get(0, 0)[0];
            if (levelWeights.rows() > 1) {
                for (int i = 1; i < levelWeights.rows(); i++) {
                    if (confindence < levelWeights.get(i, 0)[0]) {
                        confindence = levelWeights.get(i, 0)[0];

                    }
                }
            }
        }
//        object.release();
    }

    public double getConfindence() {
        return confindence;
    }

    public void setConfindence(double confindence) {
        this.confindence = confindence;
    }

    public String getName() {
        return nameLight;
    }

    public Color getColor() {
        return light;
    }
}

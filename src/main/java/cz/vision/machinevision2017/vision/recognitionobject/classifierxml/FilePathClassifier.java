package cz.vision.machinevision2017.vision.recognitionobject.classifierxml;

import cz.vision.mylog.MyLog;

/**
 * Created by Michal on 16.11.2017.
 */

public class FilePathClassifier {
    public static final String ECU = "/storage/emulated/0/Pictures/cascade_HAAR_ECU.xml";
    public static final String BATTERY = "/storage/emulated/0/Pictures/cascade_HAAR_BATTERY.xml";
    public static final String TEMP = "/storage/emulated/0/Pictures/cascade_HAAR_TEMP.xml";
    public static final String TRACTION = "/storage/emulated/0/Pictures/cascade_HAAR_TRACT.xml";
    public static final String TIRE = "/storage/emulated/0/Pictures/cascade_HAAR_TIRE.xml";
    public static void failedToLoad(String name){
        MyLog.i(MyLog.TAG, "Classifier is not loaded, probably this path"+name+" does not exist" );
    }
}

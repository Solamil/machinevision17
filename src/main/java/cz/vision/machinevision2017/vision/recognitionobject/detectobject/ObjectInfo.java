package cz.vision.machinevision2017.vision.recognitionobject.detectobject;

import org.opencv.core.Rect;

/**
 * Created by Michal on 16.11.2017.
 */

public class ObjectInfo {
    private Rect rect;
    private String name;
    public ObjectInfo(Rect rect, String name) {
        this.rect = rect;
        this.name = name;
    }
    public void setObjectRect(Rect rect){
        this.rect = rect;
    }
    public Rect getObjectRect() {
        return rect;
    }

    public String getName() {
        return name;
    }
}

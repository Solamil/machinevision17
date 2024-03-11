package cz.vision.machinevision2017.activities;

import cz.vision.machinevision2017.vision.PreprocessingImage;

/**
 * Created by Michal on 08.03.2018.
 */

public class Singleton {
    private static PreprocessingImage preprocessingImage;
    private Singleton(){}

    public static PreprocessingImage getPreprocessingImage(){
        if(preprocessingImage == null){
            preprocessingImage = new PreprocessingImage();
        }
        return preprocessingImage;
    }

}

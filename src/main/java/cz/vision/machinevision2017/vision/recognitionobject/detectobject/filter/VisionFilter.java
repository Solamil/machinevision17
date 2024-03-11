package cz.vision.machinevision2017.vision.recognitionobject.detectobject.filter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import cz.vision.machinevision2017.vision.imgprocess.Color;
import cz.vision.machinevision2017.vision.recognitionobject.classificatorI.DetectObjectListener;


public class VisionFilter {
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 1920;
    private static final int MIN_WIDTH = 70;
    private static final int MIN_HEIGHT = 120;
    private static final int MAX_SIZE = 1000;
    private static final int STANDARD_WIDTH = 170;
    private static final int STANDARD_HEIGHT = 240;

    private static final Scalar lowerRedLow = new Scalar(0, 100, 100);
    private static final Scalar upperRedLow = new Scalar(15, 255, 255);
    private static final Scalar lowerRedHigh = new Scalar(159, 100, 100);
    private static final Scalar upperRedHigh = new Scalar(179, 255, 255);
    private static final Scalar lowerYellow = new Scalar(0, 100, 100);
    private static final Scalar upperYellow = new Scalar(34, 255, 255);

    private Mat matRedLow;
    private Mat matRedHigh;
    private Mat matYellow;
    private Mat kernel;

    private List<MatOfPoint> listContours;
    private Mat hierarchy;
    private Mat imageScreen;
    private DetectObjectListener objectListener;
    private Mat objectColor;
    private Mat binaryScreen;
    private Rect rect;
    private Mat object;
    private Mat rgbSourceImage;
    private Color color;

    public VisionFilter(DetectObjectListener objectListener) {

        matRedLow = new Mat(WIDTH, HEIGHT, CvType.CV_8UC1);
        matRedHigh = new Mat(WIDTH, HEIGHT, CvType.CV_8UC1);
        matYellow = new Mat(WIDTH, HEIGHT, CvType.CV_8UC1);
        kernel = Mat.ones(5, 5, CvType.CV_8UC1);

        listContours = new ArrayList<>();

        objectColor = new Mat(WIDTH, HEIGHT, CvType.CV_8UC3);
        hierarchy = new Mat(WIDTH, HEIGHT, CvType.CV_8UC1);
        imageScreen = new Mat(WIDTH, HEIGHT, CvType.CV_8UC3);
        binaryScreen = new Mat(WIDTH, HEIGHT, CvType.CV_8UC1);
        rgbSourceImage = new Mat(WIDTH, HEIGHT, CvType.CV_8UC3);


        rect = new Rect();
        object = new Mat();
        this.objectListener = objectListener;
    }

    public void applyScreen(Mat src) {
        Imgproc.cvtColor(src, rgbSourceImage, Imgproc.COLOR_RGBA2RGB);


//        Imgproc.cvtColor(imageScreen, imageScreen, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(rgbSourceImage, imageScreen, Imgproc.COLOR_RGB2HSV);


//        Core.inRange(redMat, new Scalar(160, 100, 100), new Scalar(180, 255, 255), src);
//        Core.inRange(yellowMat, new Scalar(23, 41, 133), new Scalar(40, 150, 255), src);
        //RED
        Core.inRange(imageScreen, lowerRedLow, upperRedLow, matRedLow);
        Core.inRange(imageScreen, lowerRedHigh, upperRedHigh, matRedHigh);
        //YELLOW
        Core.inRange(imageScreen, lowerYellow, upperYellow, matYellow);

        Core.bitwise_or(matRedHigh, matYellow, binaryScreen);
        Core.bitwise_or(binaryScreen, matRedLow, binaryScreen);
        findContours(binaryScreen);
    }

    private void findContours(Mat binaryScreen){
        //problem je kdyz se prepne na informace o kontrolce tak matice binaryscreen ma jinou velikost
        // proto je tu podminka s vyskou a sirkou
        if (checkImage(binaryScreen, 20000) && binaryScreen.size().width == HEIGHT && binaryScreen.size().height == WIDTH) {
            Imgproc.morphologyEx(binaryScreen, binaryScreen, Imgproc.MORPH_OPEN, kernel);

            Imgproc.findContours(binaryScreen, listContours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            hierarchy.release();
            for (int i = 0; i < listContours.size(); i++) {
                rect = Imgproc.boundingRect(listContours.get(i));

                if (rect.width >= MIN_WIDTH && rect.height >= MIN_HEIGHT && rect.width < MAX_SIZE && rect.height < MAX_SIZE ) {


//                    MyLog.i(MyLog.TAG, rect.x + " " + rect.y + " " + rect.width + " " + rect.height);

                    object = binaryScreen.submat(rect);
                    if(checkImage(object, 5000)) {
//                        MyLog.i(MyLog.TAG, object.size().width+ " " +object.size().height);
                        Imgproc.resize(object, object, new Size(STANDARD_WIDTH, STANDARD_HEIGHT));
//                        Image.takePhoto(object);
                        objectColor = rgbSourceImage.submat(rect);// HERE MEMORY LEAK
                        Imgproc.cvtColor(objectColor, objectColor, Imgproc.COLOR_BGR2RGB);//for some reason red is blue

                        color = checkColor(objectColor);

                        objectListener.detectObject(object, color, rect);

                        object.release();
                    }
                }
            }
        }
    }

    private static boolean checkImage(Mat binaryScreen, int numberOfPixel){
        if (Core.countNonZero(binaryScreen) > numberOfPixel) {
            return true;
        }
        return false;
    }
    public static Color checkColor(Mat light){

        Mat binary = new Mat(light.rows(), light.cols(), CvType.CV_8UC1);
        //YELLOW
        Core.inRange(light, lowerYellow, upperYellow, binary);

        int counterYellow = Core.countNonZero(binary);
        //RED
        Core.inRange(light, lowerRedLow, upperRedLow, binary);
        int counterRed = Core.countNonZero(binary);

        binary.release();
        if(counterRed < counterYellow){
            return Color.YELLOW;
        }else{
            return Color.RED;

        }
    }
}

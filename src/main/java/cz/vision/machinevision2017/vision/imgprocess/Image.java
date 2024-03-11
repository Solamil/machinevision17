package cz.vision.machinevision2017.vision.imgprocess;

import android.content.ContentValues;
import android.os.Environment;
import android.provider.MediaStore;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import cz.vision.mylog.MyLog;

/**
 * Created by Michal on 16.11.2017.
 */

public class Image {
    private static final String PHOTO_MIME_TYPE = "image/png";
    private static final String PHOTO_FILE_EXTENSION = ".png";


    private Image(){}

    public static void takePhoto(Mat referenceImageGray) {
        long currentTimeMillis = System.currentTimeMillis();
        String appName = "OpenCV3";
        String galleryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        String albumPath = galleryPath + File.separator + appName;
        String photoPath = albumPath + File.separator + currentTimeMillis + PHOTO_FILE_EXTENSION;
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, photoPath);
        values.put(MediaStore.Images.Media.MIME_TYPE, PHOTO_MIME_TYPE);
        values.put(MediaStore.Images.Media.TITLE, appName);
        values.put(MediaStore.Images.Media.DESCRIPTION, appName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);

        File album = new File(albumPath);

        if (!album.isDirectory() && !album.mkdir()) {
            MyLog.i(MyLog.TAG, "Failed to create album directory at " + albumPath);

            return;
        }

        if (!Imgcodecs.imwrite(photoPath, referenceImageGray)) {
            MyLog.i(MyLog.TAG, "Failed to save photo to " + photoPath);
            return;
        }
        MyLog.i(MyLog.TAG, "Photo saved successfully  to " + photoPath);

    }

    public static void writePhotoToFile(Mat src){

        Writer writer;
        File folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS+"/textFile/");

        if(!folderPath.exists()){
            folderPath.mkdirs();
        }

        long currentTimeMillis = System.currentTimeMillis();
        File file = new File(folderPath, "photo"+currentTimeMillis+".txt");
        try {
            file.createNewFile();
            writer = new BufferedWriter(new FileWriter(file));

            for(int i = 0; i < src.rows(); i++){
                for(int j = 0; j < src.cols(); j++){
                    double[] data = src.get(i, j);
                    writer.write(String.valueOf((int) data[0])+" ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

package cz.vision.mylog;

import android.util.Log;


public class MyLog {
    public static final String TAG = "messages";
    private static final boolean t = true;
    private MyLog(){}

    public static void i(String tag, String msg){
        Log.i(tag, msg);
    }
    public static void i(String tag, int number){
        Log.i(tag, ""+number);
    }
    public static void i(String tag, String msg, Throwable throwable){
        Log.i(tag, msg, throwable);
    }
    public static void i(String tag, StringBuilder msg){
        Log.i(tag, msg.toString());
    }
    public static void i(String tag, double[] numbers){
        for(int i = 0; i < numbers.length; i++){
            Log.i(tag, ""+numbers[i]);
        }
    }
    public static void i(String tag, double number){
        Log.i(tag, ""+number);
    }
}

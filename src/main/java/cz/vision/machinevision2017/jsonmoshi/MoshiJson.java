package cz.vision.machinevision2017.jsonmoshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import cz.vision.mylog.MyLog;


public class MoshiJson {
    public static final String JSON_PATH = "/storage/emulated/0/Pictures/symbol.json";
    private static BufferedReader br;

    private MoshiJson(){}

    public static String readAll(final String filePath){
        File file = new File(filePath);
        String s = new String();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Windows-1250"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            s = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }
    public static List<Symbol> moshiJsonInfoObject(){
        String str = readAll(JSON_PATH);

        List<Symbol> symbols = null;
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Symbol.class);
        JsonAdapter<List<Symbol>>  jsonAdapter = moshi.adapter(type);
        try {
            symbols = jsonAdapter.fromJson(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return symbols;
    }
}

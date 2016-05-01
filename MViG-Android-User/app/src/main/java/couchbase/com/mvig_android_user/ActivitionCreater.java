package couchbase.com.mvig_android_user;

import java.util.Random;

/**
 * Created by Mustafa on 1.5.2016.
 */
public class ActivitionCreater {

    private static String actCode;

    public static void createCode(){
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();

        int[] actArray = new int[6];  // rakamların tutulacağı dizi

        for(int i=0; i<6; i++)
            actArray[i] = random.nextInt(10); //[0-10) arasında rasgele rakam seciliyor

        for(int i=0; i<6; i++)
            buffer.append(String.valueOf(actArray[i]));  //

        actCode = buffer.toString();
    }

    public static String getCode(){
        return actCode;
    }
}

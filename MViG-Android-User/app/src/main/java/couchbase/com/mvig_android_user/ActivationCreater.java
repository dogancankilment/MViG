package couchbase.com.mvig_android_user;

import java.util.Random;

/**
 * Created by Mustafa on 1.5.2016.
 */
public class ActivationCreater {

    private static String actCode;

    public static void createCode(){
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();

        int[] actArray = new int[6];  // rakamların tutulacağı dizi

        actArray[0] = random.nextInt(9)+1;  //aktivasyon kodunun sıfırla başlamaması için
        for(int i=1; i<6; i++)
            actArray[i] = random.nextInt(10); //[0-10) arasında rastgele rakam seciliyor.

        for(int i=0; i<6; i++)
            buffer.append(String.valueOf(actArray[i]));  //rakamlar string'e çevriliyor.

        actCode = buffer.toString();
    }

    public static String getCode(){
        return actCode;
    }
}

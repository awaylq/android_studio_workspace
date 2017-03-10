package action.eeg.yishi.dogeeg.utils;

/**
 * Created by yishikeji_04 on 2017/2/6.
 */

public class Info {
    private static String deviceId=null;
    private static String DogId=null;

    public static String getDogId() {
        return DogId;
    }

    public static String getDeviceId() {
        return deviceId;
    }

    public static void setDogId(String dogId) {
        DogId = dogId;
    }

    public static void setDeviceId(String deviceId) {
        Info.deviceId = deviceId;
    }


}

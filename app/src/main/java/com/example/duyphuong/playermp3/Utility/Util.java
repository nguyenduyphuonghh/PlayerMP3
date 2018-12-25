package com.example.duyphuong.playermp3.Utility;

public class Util {

    // chuyen tu milliseconds sang hh:mm:ss

    public static String millisecondsToTimer(long milliseconds)
    {
        String finalTimerString = "";
        String secondsString = "";

        // chuyen doi toan boj thoi luong sang time

        int hours = (int) (milliseconds / (1000*60*60));
        int minutes = (int) (milliseconds % (1000*60*60) / (1000*60));
        int seconds = (int) (milliseconds % (1000*60*60) % (1000*60) / 1000);

        // neu seconds < 10 thi them so "0" vao truoc

        if (seconds < 10)
        {
            secondsString = "0" + seconds;
        } else
        {
            secondsString = "" + seconds;
        }

        if (hours > 0) {
            finalTimerString = hours + ":" + minutes + ":" + secondsString;
        } else {
            finalTimerString =  minutes + ":" + secondsString;
        }

        return finalTimerString;

    }

    // tinh phan tram bai hat da chay duoc

    public static  int getProgressPercentage (long currentDuration, long totalDuration)
    {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        return percentage.intValue();
    }

    /**
     * chuyen gia tri seekbar sang dang time
     * @param progress
     * @param totalDuration
     * @return
     */
    public static int progressToTimer(int progress, int totalDuration)
    {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }
}

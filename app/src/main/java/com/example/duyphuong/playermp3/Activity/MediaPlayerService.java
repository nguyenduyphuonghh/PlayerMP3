package com.example.duyphuong.playermp3.Activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.example.duyphuong.playermp3.Model.SongModel;
import com.example.duyphuong.playermp3.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MediaPlayerService extends Service {

    private MediaSession mSession;
    public static final String ACTION_PLAY = "action_play";
    public static final String MAIN_CLICK_ACTION_PLAY = "action_main_play";
    public static final String MAIN_CLICK_ACTION_STOP = "action_main_stop";
    public static final String MAIN_CLICK_ACTION_PREVIOUS = "action_main_previous";
    public static final String MAIN_CLICK_ACTION_NEXT = "action_main_next";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_forward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaSessionManager mManager;
    private MediaController mController;

    public MediaPlayer mediaPlayer;
    public ArrayList<SongModel> arrSong = new ArrayList<>();

    MainActivity mainActivity = new MainActivity();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleIntent(Intent intent) {
        if(intent == null || intent.getAction() == null) {
            return;
        }
        String action = intent.getAction();
        if (action.equalsIgnoreCase(MAIN_CLICK_ACTION_STOP)) {
            MainActivity_ClickPlay();
        }

        else if (action.equalsIgnoreCase(MAIN_CLICK_ACTION_PLAY)) {
            MainActivity_ClickPause();
        }

        else if (action.equalsIgnoreCase(MAIN_CLICK_ACTION_NEXT)) {
            MainActivity_ClickPrevNext();
        }

        else if (action.equalsIgnoreCase(MAIN_CLICK_ACTION_PREVIOUS)) {
            MainActivity_ClickPrevNext();
        }
        else if(action.equalsIgnoreCase(ACTION_PLAY)) {
            mController.getTransportControls().play();
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    // doi thanh btn play
                    mainActivity.btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    mainActivity.imageViewCenter.setImageResource(R.drawable.audiopause);
                } else {
                    // tiep tuc bai hat
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        // doi thanh pause
                        mainActivity.btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                        mainActivity.imageViewCenter.setImageResource(R.drawable.audiogif);
                    }
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mainActivity.tvCurrentDuration.setAnimation(null);
                    // doi thanh btn play
                    mainActivity.btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    mainActivity.imageViewCenter.setImageResource(R.drawable.audiogif);
                }
            }

        } else if(action.equalsIgnoreCase(ACTION_PAUSE)) {
            mController.getTransportControls().pause();
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    mainActivity.tvCurrentDuration.setAnimation(AnimationUtils.loadAnimation(this,R.anim.flash));
                    // doi thanh btn play
                    mainActivity.btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    mainActivity.imageViewCenter.setImageResource(R.drawable.audiopause);
                } else {
                    // tiep tuc bai hat
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        // doi thanh pause
                        mainActivity.btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                        mainActivity.imageViewCenter.setImageResource(R.drawable.audiogif);
                    }
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    // doi thanh btn play
                    mainActivity.btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    mainActivity.imageViewCenter.setImageResource(R.drawable.audiogif);
                }
            }
        } else if(action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
            mController.getTransportControls().fastForward();
            int currentSongPosition = mediaPlayer.getCurrentPosition();
            if (currentSongPosition + mainActivity.seekForwardTime <= mediaPlayer.getDuration()) {
                //forward song
                mediaPlayer.seekTo(currentSongPosition + mainActivity.seekForwardTime);
            } else {
                // forward den vi tri cuoi cung
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        } else if(action.equalsIgnoreCase(ACTION_REWIND)) {
            mController.getTransportControls().rewind();
            int currentSongPosition = mediaPlayer.getCurrentPosition();

            if (currentSongPosition - mainActivity.seekBackwardTime >= 0) {
                mediaPlayer.seekTo(currentSongPosition - mainActivity.seekBackwardTime);
            } else {
                mediaPlayer.seekTo(0);
            }
        } else if(action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            mController.getTransportControls().skipToPrevious();
            if (mainActivity.currentSongIndex > 0) {
                playSong(mainActivity.currentSongIndex - 1);
                mainActivity.currentSongIndex = mainActivity.currentSongIndex - 1;
            } else {
                playSong(arrSong.size() - 1);
                mainActivity.currentSongIndex = arrSong.size() - 1;
            }
        } else if(action.equalsIgnoreCase(ACTION_NEXT)) {
            mController.getTransportControls().skipToNext();
            if (mainActivity.currentSongIndex < (arrSong.size() - 1)) {
                playSong(mainActivity.currentSongIndex + 1);
                mainActivity.currentSongIndex = mainActivity.currentSongIndex + 1;
            } else {
                playSong(0);
                mainActivity.currentSongIndex = 0;
            }
        } else if(action.equalsIgnoreCase(ACTION_STOP)) {
            mController.getTransportControls().stop();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mainActivity.RepeatThisSong == false && MainActivity.RandomSong == false) {
                    if (mainActivity.currentSongIndex < (arrSong.size() - 1)) {
                        playSong(mainActivity.currentSongIndex + 1);
                        mainActivity.currentSongIndex = mainActivity.currentSongIndex + 1;
                    } else {
                        playSong(0);
                        mainActivity.currentSongIndex = 0;
                    }
                } else if (mainActivity.RepeatThisSong == true && mainActivity.RandomSong == false) {
                    playSong(mainActivity.currentSongIndex);
                } else {
                    Random random = new Random();
                    playSong(random.nextInt(arrSong.size()));
                }
                buildNotification(generateAction(android.R.drawable.ic_media_pause,
                        "Pause", ACTION_PAUSE));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public void MainActivity_ClickPause() {
        buildNotification(generateAction(android.R.drawable.ic_media_pause,"Play",ACTION_PAUSE));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void MainActivity_ClickPlay() {
        buildNotification(generateAction(android.R.drawable.ic_media_play,"Play",ACTION_PLAY));
    }
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void MainActivity_ClickPrevNext() {
        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void buildNotification(Notification.Action action) {
        Notification.MediaStyle style = new Notification.MediaStyle();
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icon_notifi)
                .setColor(getResources().getColor(R.color.MainAppColor))
                .setContentTitle(arrSong.get(mainActivity.currentSongIndex).title)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setStyle(style);

        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Forward", ACTION_FAST_FORWARD));
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
        style.setShowActionsInCompactView(0, 1, 2, 3, 4);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        arrSong = mainActivity.arrSong;
        mediaPlayer = mainActivity.mediaPlayer;

        if(mManager == null) {
            initMediaSession();
        }

        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void playSong(int currentSongIndex) {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrSong.get(currentSongIndex).path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mainActivity.toolbar.setTitle(mainActivity.arrSong.get(currentSongIndex).title);
            mainActivity.btnPlay.setImageResource(android.R.drawable.ic_media_pause);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initMediaSession() {
        mSession = new MediaSession(getApplicationContext(), "example player session");
        mController = new MediaController(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback(){
            @Override
            public void onPlay() {
                super.onPlay();
                buildNotification(generateAction(android.R.drawable.ic_media_pause,"Pause", ACTION_PAUSE));
            }

            @Override
            public void onPause() {
                super.onPause();
                buildNotification(generateAction(android.R.drawable.ic_media_play,
                        "Pause", ACTION_PLAY));
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                buildNotification(generateAction(android.R.drawable.ic_media_pause,
                        "Pause", ACTION_PAUSE));
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                buildNotification(generateAction(android.R.drawable.ic_media_pause,
                        "Pause", ACTION_PAUSE));
                Log.i("current song",arrSong.get(mainActivity.currentSongIndex).title);
            }

            @Override
            public void onFastForward() {

                super.onFastForward();
            }

            @Override
            public void onRewind() {
                super.onRewind();
            }

            @Override
            public void onStop() {
                super.onStop();
                NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
                Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
                stopService(intent);
            }
        });
    }
}

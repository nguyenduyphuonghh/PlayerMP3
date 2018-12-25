package com.example.duyphuong.playermp3.Activity;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.duyphuong.playermp3.Adapter.SongsManager;
import com.example.duyphuong.playermp3.Model.SongModel;
import com.example.duyphuong.playermp3.R;
import com.example.duyphuong.playermp3.Utility.FIleUtils;
import com.example.duyphuong.playermp3.Utility.Util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.io.File;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, PopupMenu.OnMenuItemClickListener {

    public static final int SELECT_SONG_REQUEST = 0;
    public static int RESULT_LOAD_IMAGE = 1;
    private static final int BROWSE_FILE_REQUEST = 3;
    public static Toolbar toolbar;

    public static ArrayList<SongModel> arrSong = new ArrayList<>();

//    SongModel song = new SongModel();

    public static int seekForwardTime = 5000;
    public static int seekBackwardTime = 5000;
    public static int currentSongIndex = 0;

    public static MediaPlayer mediaPlayer;

    public Handler mediaPlayerHandler = new Handler();

    public Handler stopMp = new Handler();

    public static ImageView btnPlay;
    private ImageView btnForward;
    private ImageView btnBackward;
    private ImageView btnBackSong;
    private ImageView btnNexSong;
    private Button btnRandom;
    private Button btnRepeat;
    private Button btnMinAudio;
    private Button btnMaxAudio;
    private Button btnTimer;
    private Button btnMore;
    public static Boolean RepeatThisSong = false;
    public static Boolean RandomSong = false;
    public static ImageView imageViewCenter;

    private TextView tvTimerTicking;
    private Switch swTimer;
    private RadioGroup radioGroupTimer;
    private RadioButton rdb30, rdb60, rdb90, rdb120;
    private Button btnCloseTimer;
    public static int timerSet = 1;
    private Dialog dialogShowTimer;

    private SeekBar seekBar;
    private SeekBar seekBarVolume;
    private AudioManager audioManager;

    public static TextView tvCurrentDuration;
    private TextView tvTotalDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFile();
    }

    private void loadControls() {
        btnPlay = findViewById(R.id.btnPlay);
        btnForward = findViewById(R.id.btnForward);
        btnBackward = findViewById(R.id.btnBackward);
        btnBackSong = findViewById(R.id.btnBackSong);
        btnNexSong = findViewById(R.id.btnNextSong);
        imageViewCenter = findViewById(R.id.imgCenter);
        seekBar = findViewById(R.id.seekBarContent);
        tvCurrentDuration = findViewById(R.id.txtvCurrentTime);
        tvTotalDuration = findViewById(R.id.tvTotalTime);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnRandom = findViewById(R.id.btnRandom);
        btnMinAudio = findViewById(R.id.btnMinAudio);
        btnMaxAudio = findViewById(R.id.btnMaxAudio);
        btnTimer = findViewById(R.id.btnTimer);
        btnMore  = findViewById(R.id.btnMore);
    }

    public void loadEvents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mediaPlayer = new MediaPlayer();

        btnNexSong.setOnClickListener(this);
        btnBackSong.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnRandom.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnMinAudio.setOnClickListener(this);
        btnMaxAudio.setOnClickListener(this);
        btnTimer.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        imageViewCenter = findViewById(R.id.imgCenter);
        seekBar.setOnSeekBarChangeListener(this);

        startPlaySong(currentSongIndex);
        mediaPlayer.setOnCompletionListener(this);

        volumeControls();

//        buildNotification();
    }

    private void checkFile() {
        SongsManager songsManager = new SongsManager();
        arrSong = songsManager.getPlayList();
        if (arrSong != null) {
            loadControls();
            loadEvents();
        } else {

        }
    }

    public void fileNotFound(String e) {
        toolbar.setTitle("" + e);
    }

    private void volumeControls() {
        // volume control
        seekBarVolume = findViewById(R.id.seekBarVolume);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void getFileLocation() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void buildNotification() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icon_notifi)
                .setContentTitle("Media")
                .setContentText(arrSong.get(currentSongIndex).title)
                .setDeleteIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void startPlaySong(int currentSongIndex) {

        try {
            mediaPlayer.setDataSource(arrSong.get(currentSongIndex).path);
            mediaPlayer.prepare();
//            mediaPlayer.start();

            // cap nhat ten bai hat len toolbar
            toolbar.setTitle(arrSong.get(currentSongIndex).title);

//            btnPlay.setImageResource(android.R.drawable.ic_media_play);
//
//            // cai dat gia tri cho seekbar
//
            seekBar.setProgress(0);
            seekBar.setMax(100);

//            // cap nhat seekbar
//
//            updateSeekBar();

//            buildNotification();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void playSong(int currentSongIndex) {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrSong.get(currentSongIndex).path);
            mediaPlayer.prepare();
            mediaPlayer.start();

            // cap nhat ten bai hat len toolbar
            toolbar.setTitle(arrSong.get(currentSongIndex).title);

            // doi play sang pause

            btnPlay.setImageResource(android.R.drawable.ic_media_pause);

            imageViewCenter.setImageResource(R.drawable.audiogif);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void updateSeekBar() {
        mediaPlayerHandler.postDelayed(mediaPlayerUpdateTimeTask, 100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_songs:
                Intent intent = new Intent(this, ActivityListSongs.class);
                startActivityForResult(intent, SELECT_SONG_REQUEST);
                break;
            default:
                PopupMenu popupMenu2 = new PopupMenu(this,findViewById(R.id.toolbar));
                popupMenu2.inflate(R.menu.popup_menu);
                popupMenu2.show();
                popupMenu2.setOnMenuItemClickListener(this);
        }
        return true;
    }
    // xu ly ket qua tra ve tu activity list song

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ket qua tra ve khi chon tu danh sach bai hat
        if (requestCode == SELECT_SONG_REQUEST && resultCode == RESULT_OK) {
            currentSongIndex = data.getExtras().getInt("id");
            playSong(currentSongIndex);
            Intent intentMediaPlayerService = new Intent(this, MediaPlayerService.class);
            intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_NEXT);
            startService(intentMediaPlayerService);
        }
    }

    private Runnable stopPlayBack = new Runnable() {
        @Override
        public void run() {
            try {
                mediaPlayer.pause();
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
                imageViewCenter.setImageResource(R.drawable.audiopause);
                tvCurrentDuration.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.flash));
                Intent intentMediaPlayerService = new Intent(MainActivity.this, MediaPlayerService.class);
                intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_STOP);
                startService(intentMediaPlayerService);
                Toast.makeText(MainActivity.this, "Timer completed", Toast.LENGTH_SHORT).show();
                swTimer.setChecked(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable mediaPlayerUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            try {
                long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();

                // hien thi textview thoi gian

                tvCurrentDuration.setText("" + Util.millisecondsToTimer(currentDuration));
                tvTotalDuration.setText("" + Util.millisecondsToTimer(totalDuration));

                // cap nhat seekbar

                int progress = (Util.getProgressPercentage(currentDuration,totalDuration));
                seekBar.setProgress(progress);

                // chay handler sau moi 100 milliseconds ( this = mediaPlayerUpdateTimeTtask)

                mediaPlayerHandler.postDelayed(this,100);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Keep playing in background?")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setTitle("Attention!")
                .setCancelable(false)
                .setPositiveButton("Keep", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.header_menu,menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnPlay) {
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    // doi thanh btn play
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    imageViewCenter.setImageResource(R.drawable.audiopause);
                    tvCurrentDuration.setAnimation(AnimationUtils.loadAnimation(this,R.anim.flash));
                    Intent intentMediaPlayerService = new Intent(this, MediaPlayerService.class);
                    intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_STOP);
                    startService(intentMediaPlayerService);
                } else {
                    // tiep tuc bai hat
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        // doi thanh pause
                        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                        imageViewCenter.setImageResource(R.drawable.audiogif);
                        tvCurrentDuration.setAnimation(null);
                        Intent intentMediaPlayerService = new Intent(this, MediaPlayerService.class);
                        intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_PLAY);
                        startService(intentMediaPlayerService);
                    }
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    updateSeekBar();
                    tvCurrentDuration.setAnimation(null);
                    // doi thanh btn play
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    imageViewCenter.setImageResource(R.drawable.audiogif);
                    Intent intentMediaPlayerService = new Intent(this, MediaPlayerService.class);
                    intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_PLAY);
                    startService(intentMediaPlayerService);
                }
            }
        }

        if (id == R.id.btnForward) {
            int currentSongPosition = mediaPlayer.getCurrentPosition();
            if (currentSongPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                //forward song
                mediaPlayer.seekTo(currentSongPosition + seekForwardTime);
            } else {
                // forward den vi tri cuoi cung
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        }

        if (id == R.id.btnBackward) {
            int currentSongPosition = mediaPlayer.getCurrentPosition();

            if (currentSongPosition - seekBackwardTime >= 0) {
                mediaPlayer.seekTo(currentSongPosition - seekBackwardTime);
            } else {
                mediaPlayer.seekTo(0);
            }
        }

        if (id == R.id.btnNextSong) {
            if (currentSongIndex < (arrSong.size() - 1)) {
                currentSongIndex = currentSongIndex + 1;
                playSong(currentSongIndex);
                Intent intentMediaPlayerService = new Intent(this, MediaPlayerService.class);
                intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_NEXT);
                startService(intentMediaPlayerService);
            } else {
                currentSongIndex = 0;
                // neu den cuoi danh sach thi quay lai bai hat dau
                playSong(currentSongIndex);
                Intent intentMediaPlayerService = new Intent(this, MediaPlayerService.class);
                intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_NEXT);
                startService(intentMediaPlayerService);
            }
        }

        if (id == R.id.btnBackSong) {
            Intent intentMediaPlayerService = new Intent(this, MediaPlayerService.class);
            if (currentSongIndex > 0) {
                currentSongIndex = currentSongIndex - 1;
                playSong(currentSongIndex);
                intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_PREVIOUS);
                startService(intentMediaPlayerService);
            } else {
                currentSongIndex = arrSong.size() - 1;
                playSong(currentSongIndex);
                intentMediaPlayerService.setAction(MediaPlayerService.MAIN_CLICK_ACTION_PREVIOUS);
                startService(intentMediaPlayerService);
            }
        }
        if (id == R.id.btnRepeat) {
            if (RepeatThisSong == true) {
                Toast.makeText(this, "Off repeat", Toast.LENGTH_SHORT).show();
                RepeatThisSong = false;
                btnRepeat.setBackgroundResource(R.drawable.repeatall);

            } else if (RepeatThisSong == false) {
                Toast.makeText(this, "On repeat", Toast.LENGTH_SHORT).show();
                RepeatThisSong = true;
                btnRepeat.setBackgroundResource(R.drawable.repeatthissong);
            }
        }
        if (id == R.id.btnRandom) {
            if (RandomSong == false) {
                RandomSong = true;
                btnRandom.setBackgroundResource(R.drawable.randomactivated);
                Toast.makeText(this, "Playing random", Toast.LENGTH_SHORT).show();
            } else if (RandomSong == true) {
                RandomSong = false;
                btnRandom.setBackgroundResource(R.drawable.random);
                Toast.makeText(this, "Playing sequently", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.btnMinAudio) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
        if (id == R.id.btnMaxAudio) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10,0);
            seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
        if (id == R.id.btnTimer) {
            showDialogTimer();
        }
        if (id == R.id.btnMore) {
            Intent intent = new Intent(this, EqualizerActivity.class);
            startActivity(intent);
        }
        if (id == R.id.tvClickCustomTimer) {
            showDialogPickTimer();
        }
    }

    private void showDialogTimer() {
        dialogShowTimer = new Dialog(this);
        dialogShowTimer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogShowTimer.setContentView(R.layout.timer_dialog);

        TextView tvClickCustomTimer = dialogShowTimer.findViewById(R.id.tvClickCustomTimer);
        tvClickCustomTimer.setOnClickListener(this);
        tvTimerTicking = dialogShowTimer.findViewById(R.id.tvTimerTicking);
        swTimer = dialogShowTimer.findViewById(R.id.swTimer);
        radioGroupTimer = dialogShowTimer.findViewById(R.id.rdgTimer);
        rdb30 = dialogShowTimer.findViewById(R.id.rdb30);
        rdb60 = dialogShowTimer.findViewById(R.id.rdb60);
        rdb90 = dialogShowTimer.findViewById(R.id.rdb90);
        rdb120 = dialogShowTimer.findViewById(R.id.rdb120);
        btnCloseTimer = dialogShowTimer.findViewById(R.id.btnCloseTimer);
        btnCloseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowTimer.dismiss();
            }
        });

        // listen to what rdb is checked
        radioGroupTimer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (radioGroupTimer.getCheckedRadioButtonId()) {
                    case R.id.rdb30:
                        timerSet = 10;
                        Toast.makeText(MainActivity.this, "Player will stop after 10 seconds", Toast.LENGTH_SHORT).show();
                        swTimer.setChecked(false);
                        swTimer.setChecked(true);
                        break;
                    case R.id.rdb60:
                        timerSet = 60 * 60;
                        Toast.makeText(MainActivity.this, "Player will stop after 60 minutes", Toast.LENGTH_SHORT).show();
                        swTimer.setChecked(false);
                        swTimer.setChecked(true);
                        break;
                    case R.id.rdb90:
                        timerSet = 90 * 60;
                        Toast.makeText(MainActivity.this, "Player will stop after 90 minutes", Toast.LENGTH_SHORT).show();
                        swTimer.setChecked(false);
                        swTimer.setChecked(true);
                        break;
                    case R.id.rdb120:
                        timerSet = 120 * 60;
                        Toast.makeText(MainActivity.this, "Player will stop after 120 minutes", Toast.LENGTH_SHORT).show();
                        swTimer.setChecked(false);
                        swTimer.setChecked(true);
                        break;
                }
            }
        });

        swTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (timerSet != 1) {
                    stopMp.postDelayed(stopPlayBack, 1000 * timerSet);
                } else {
                    showDialogPickTimer();
                }

            }
        });

        dialogShowTimer.show();
    }

    public void showDialogPickTimer() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_timer_pick_dialog);
        dialog.setCancelable(false);
        dialog.show();
        final EditText edtCustomTimer = dialog.findViewById(R.id.edtCustomTimer);
        Button btnOkCustomTimer = dialog.findViewById(R.id.btnOkCustomTimer);
        Button btnCancelCustomTimer = dialog.findViewById(R.id.btnCancelCustomTimer);
        btnOkCustomTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCustomTimer.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please input timer", Toast.LENGTH_SHORT).show();
                } else {
                    timerSet = Integer.parseInt(edtCustomTimer.getText().toString()) * 60;
                    stopMp.postDelayed(stopPlayBack, 1000 * timerSet);
                    Toast.makeText(MainActivity.this, "Player will stop after " + timerSet/60 + " minutes", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dialogShowTimer.dismiss();
                }
            }
        });
        btnCancelCustomTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // huy Handler tu cap nhat seekbar
        mediaPlayerHandler.removeCallbacks(mediaPlayerUpdateTimeTask);

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayerHandler.removeCallbacks(mediaPlayerUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = Util.progressToTimer(seekBar.getProgress(), totalDuration);

        mediaPlayer.seekTo(currentPosition);

        updateSeekBar();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        if (RepeatThisSong == false && RandomSong == false) {
//            if (currentSongIndex < (arrSong.size() - 1)) {
//                playSong(currentSongIndex + 1);
//                currentSongIndex = currentSongIndex + 1;
//            } else {
//                playSong(0);
//                currentSongIndex = 0;
//            }
//        } else if (RepeatThisSong == true && RandomSong == false) {
//            playSong(currentSongIndex);
//        } else {
//            Random random = new Random();
//            playSong(random.nextInt(arrSong.size()));
//        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mp_timer:
                Intent intentt = new Intent(MainActivity.this,IntroduceActivity.class);
                startActivity(intentt);
                break;
            case R.id.infoApp:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}

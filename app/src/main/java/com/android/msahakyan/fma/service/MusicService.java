package com.android.msahakyan.fma.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.model.Track;
import com.android.msahakyan.fma.player.IMusicPlayback;
import com.android.msahakyan.fma.util.ActionConstants;
import com.android.msahakyan.fma.util.AppUtils;
import com.android.msahakyan.fma.util.LoadNeighborTrackListener;
import com.android.msahakyan.fma.util.LoadNeighborTrackListener.AdjacentMode;
import com.android.msahakyan.fma.util.MediaPlayerPreparedListener;
import com.android.msahakyan.fma.util.MediaPlayerStateChangeListener;

import timber.log.Timber;

/**
 * @author msahakyan
 */
public class MusicService extends Service implements
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener, IMusicPlayback {

    private static final int NOTIFICATION_ID = 1001;

    private final IBinder mBinder = new MusicBinder();

    private MediaPlayerPreparedListener mPreparedListener;
    private MediaPlayerStateChangeListener mStateChangeListener;
    private LoadNeighborTrackListener mLoadNeighborTrackListener;

    private MediaPlayer mPlayer;
    private boolean mPrepared;
    private Track mTrack;
    private String mTrackTitle;
    private boolean mShuffle;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mPrepared = false;
        initMusicPlayer();
    }

    private void initMusicPlayer() {
        // Set player properties
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Set listeners
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public void setPreparedListener(MediaPlayerPreparedListener preparedListener) {
        mPreparedListener = preparedListener;
    }

    public void setStateChangeListener(MediaPlayerStateChangeListener stateChangeListener) {
        mStateChangeListener = stateChangeListener;
    }

    public void setLoadNeighborTrackListener(LoadNeighborTrackListener loadNeighborTrackListener) {
        mLoadNeighborTrackListener = loadNeighborTrackListener;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // Release resources here
        mPlayer.stop();
        mPlayer.release();
        return false;
    }

    @Override
    public void playTrack() {
        mPlayer.reset();
        Track playingTrack = mTrack;
        mTrackTitle = playingTrack.getTitle();

        // Set the data source
        try {
            mPlayer.setDataSource(playingTrack.getListenUrl());
        } catch (Exception e) {
            Timber.e(e, "Error setting data source");
        }
        try {
            mPrepared = false;
            mPlayer.prepareAsync();
        } catch (Exception e) {
            Timber.w("Can't prepare media player, exception: " + e);
        }
    }

    @Override
    public void setTrack(Track track) {
        mTrack = track;
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        //check if playback has reached the end of a track
        if (mPlayer.getCurrentPosition() > 0) {
            player.reset();
            if (mLoadNeighborTrackListener != null) {
                mLoadNeighborTrackListener.loadNeighborTrack(LoadNeighborTrackListener.NEXT);
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        player.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        mPrepared = true;
        // Start player
        player.start();
        showPlaybackNotification();
        if (mPreparedListener != null) {
            mPreparedListener.onPlayerPrepared();
        }
    }

    @Override
    public void showPlaybackNotification() {
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
            .setSmallIcon(R.drawable.notification_image_small)
            .setContentTitle(getString(R.string.playing))
            .setTicker(mTrackTitle)
            .build();

        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if (AppUtils.isJellyBeanOrLater()) {
            notification.bigContentView = expandedView;
        }

        Bitmap trackBitmap = mTrack.getImageBitmap();
        if (trackBitmap != null) {
            notification.contentView.setImageViewBitmap(R.id.image_view_track, trackBitmap);
            if (AppUtils.isJellyBeanOrLater()) {
                notification.bigContentView.setImageViewBitmap(R.id.image_view_track, trackBitmap);
            }
        } else {
            notification.contentView.setImageViewResource(R.id.image_view_track, R.drawable.img_placeholder);
            if (AppUtils.isJellyBeanOrLater()) {
                notification.bigContentView.setImageViewResource(R.id.image_view_track, R.drawable.img_placeholder);
            }
        }

        if (!isPlaying()) {
            notification.contentView.setViewVisibility(R.id.btn_pause, View.GONE);
            notification.contentView.setViewVisibility(R.id.btn_play, View.VISIBLE);

            if (AppUtils.isJellyBeanOrLater()) {
                notification.bigContentView.setViewVisibility(R.id.btn_pause, View.GONE);
                notification.bigContentView.setViewVisibility(R.id.btn_play, View.VISIBLE);
            }
        } else {
            notification.contentView.setViewVisibility(R.id.btn_pause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.btn_play, View.GONE);

            if (AppUtils.isJellyBeanOrLater()) {
                notification.bigContentView.setViewVisibility(R.id.btn_pause, View.VISIBLE);
                notification.bigContentView.setViewVisibility(R.id.btn_play, View.GONE);
            }
        }

        notification.contentView.setTextViewText(R.id.text_song_name, mTrack.getTitle());
        notification.contentView.setTextViewText(R.id.text_album_name, mTrack.getAlbumTitle());

        if (AppUtils.isJellyBeanOrLater()) {
            notification.bigContentView.setTextViewText(R.id.text_song_name, mTrack.getTitle());
            notification.bigContentView.setTextViewText(R.id.text_album_name, mTrack.getAlbumTitle());
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID, notification);

        if (mStateChangeListener != null) {
            mStateChangeListener.onPlayerStateChanged(isPlaying());
        }
    }

    /**
     * Notification listeners
     *
     * @param view The {@link RemoteViews} remoteView
     */
    public void setListeners(RemoteViews view) {

        Intent play = new Intent(ActionConstants.ACTION_PLAY);
        Intent pause = new Intent(ActionConstants.ACTION_PAUSE);
        Intent next = new Intent(ActionConstants.ACTION_NEXT);
        Intent previous = new Intent(ActionConstants.ACTION_PREVIOUS);
        Intent delete = new Intent(ActionConstants.ACTION_DELETE);

        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_play, pendingIntentPlay);

        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_pause, pendingIntentPause);

        PendingIntent pendingIntentDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_delete, pendingIntentDelete);

        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_previous, pendingIntentPrevious);

        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_next, pendingIntentNext);
    }

    // Playback methods
    @Override
    public int getPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPrepared ? mPlayer.getDuration() : 0;
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void pausePlayer() {
        mPlayer.pause();
        showPlaybackNotification();
    }

    public void resetPlayer() {
        mPlayer.reset();
    }

    @Override
    public void seek(int position) {
        mPlayer.seekTo(position);
    }

    @Override
    public void startPlayer() {
        mPlayer.start();
        showPlaybackNotification();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    // Toggle shuffle
    public void toggleShuffle() {
        mShuffle = !mShuffle;
    }

    public void cancelOngoingNotification() {
        mPlayer.pause();
        stopForeground(true);
        if (mStateChangeListener != null) {
            mStateChangeListener.onPlayerStateChanged(false);
        }
    }

    public void loadNeighborTrack(@AdjacentMode int mode) {
        if (mLoadNeighborTrackListener != null) {
            mLoadNeighborTrackListener.loadNeighborTrack(mode);
        }
    }
}

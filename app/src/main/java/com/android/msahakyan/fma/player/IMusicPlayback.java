package com.android.msahakyan.fma.player;

import android.media.MediaPlayer;

import com.android.msahakyan.fma.model.Track;

/**
 * Created by msahakyan on 01/09/16.
 */

public interface IMusicPlayback {

    /**
     * Plays a song
     */
    void playTrack();

    /**
     * Sets the track
     *
     * @param track The track which should be played
     */
    void setTrack(Track track);

    /**
     * Shows music playback in status bar
     */
    void showPlaybackNotification();

    /**
     * Returns {@link MediaPlayer#getCurrentPosition()} (in milliseconds)
     *
     * @return <code>int</code>
     */
    int getPosition();

    /**
     * Returns duration (in milliseconds)
     * {@link MediaPlayer#getDuration()}} method
     *
     * @return <code>int</code>
     */
    int getDuration();

    /**
     * Returns true/false regarding to player {@link MediaPlayer}
     * is playing now or not
     *
     * @return <code>boolean</code>
     */
    boolean isPlaying();

    /**
     * Pauses a player {@link MediaPlayer}
     */
    void pausePlayer();

    /**
     * Seek a player to position (in milliseconds)
     *
     * @param position Position is offset in milliseconds from the start to seek to
     */
    void seek(int position);

    /**
     * Starts a player {@link MediaPlayer}
     */
    void startPlayer();
}

package com.android.msahakyan.fma.util;

/**
 * Created by msahakyan on 10/09/16.
 */

public interface MediaPlayerStateChangeListener {

    /**
     * Calls when {@link android.media.MediaPlayer} state changes
     */
    void onPlayerStateChanged(boolean isPlaying);
}

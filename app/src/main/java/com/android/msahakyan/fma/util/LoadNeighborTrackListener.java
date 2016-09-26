package com.android.msahakyan.fma.util;

import android.support.annotation.IntDef;

/**
 * Created by msahakyan on 15/09/16.
 */

public interface LoadNeighborTrackListener {

    int PREVIOUS = 0;
    int NEXT = 1;

    @IntDef({PREVIOUS, NEXT})
    @interface AdjacentMode {
    }

    /**
     * Calls when a neighbor track is requested
     *
     * @param mode The previous/next
     */
    void loadNeighborTrack(@AdjacentMode int mode);
}

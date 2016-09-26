package com.android.msahakyan.fma.receiver;

/**
 * Created by msahakyan on 10/09/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.android.msahakyan.fma.service.MusicService;
import com.android.msahakyan.fma.util.ActionConstants;
import com.android.msahakyan.fma.util.LoadNeighborTrackListener;

import timber.log.Timber;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        IBinder binder = peekService(context, new Intent(context, MusicService.class));
        if (binder == null) {
            return;
        }
        MusicService service = ((MusicService.MusicBinder) binder).getService();

        if (service == null) {
            Timber.w("Service is null -- skip");
            return;
        }

        String action = intent.getAction();
        switch (action) {
            case ActionConstants.ACTION_PLAY:
                service.startPlayer();
                break;
            case ActionConstants.ACTION_PAUSE:
                service.pausePlayer();
                break;
            case ActionConstants.ACTION_NEXT:
                service.loadNeighborTrack(LoadNeighborTrackListener.NEXT);
                break;
            case ActionConstants.ACTION_PREVIOUS:
                service.loadNeighborTrack(LoadNeighborTrackListener.PREVIOUS);
                break;
            case ActionConstants.ACTION_DELETE:
                service.cancelOngoingNotification();
                break;
            default:
                break;
        }
    }
}


package com.android.msahakyan.fma.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.model.Track;
import com.android.msahakyan.fma.network.NetworkManager;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.service.MusicDownloaderService;
import com.android.msahakyan.fma.service.MusicService;
import com.android.msahakyan.fma.util.AppUtils;
import com.android.msahakyan.fma.util.BitmapUtils;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.util.ItemLoadedListener;
import com.android.msahakyan.fma.util.LoadNeighborTrackListener;
import com.android.msahakyan.fma.util.MediaPlayerPreparedListener;
import com.android.msahakyan.fma.util.MediaPlayerStateChangeListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackDetailFragment extends BaseItemDetailFragment<Track> implements View.OnClickListener, View.OnTouchListener,
    ItemLoadedListener, MediaPlayerPreparedListener, MediaPlayerStateChangeListener, LoadNeighborTrackListener {

    private static final String KEY_ITEM_LIST = "KEY_ITEM_LIST";
    private static final String KEY_ITEM_POSITION = "KEY_ITEM_POSITION";

    @Bind(R.id.track_image)
    ImageView mTrackImageView;
    @Bind(R.id.track_detail_image_container)
    ImageView mTrackImageContainer;
    @Bind(R.id.track_title)
    TextView mTitleView;

    @Bind(R.id.button_play_pause)
    ImageView mButtonPlayPause;
    @Bind(R.id.button_previous)
    ImageView mButtonPrevious;
    @Bind(R.id.button_next)
    ImageView mButtonNext;
    @Bind(R.id.seek_bar)
    SeekBar mProgress;
    @Bind(R.id.progress_passed)
    TextView mProgressPassed;
    @Bind(R.id.song_time)
    TextView mSongTime;
    @Bind(R.id.button_volume)
    ImageView mButtonVolume;
    @Bind(R.id.progress_detail_view)
    CircularProgressView mProgressView;
    @Bind(R.id.license)
    ImageView mLicense;

    private MusicService mMusicService;
    private final Handler handler;
    private Intent mPlayIntent;
    private boolean mIsBound = false;
    private int mDurationInMillis;
    private boolean mMute;
    private boolean mPaused;
    private boolean mPrepared;
    private List<Track> mTracks;
    private int mListPosition;

    private boolean mPlaybackPaused;
    private NetworkRequestListener<Item> mNetworkRequestListener;

    public TrackDetailFragment() {
        handler = new Handler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tracks The list of tracks in the current {@link com.android.msahakyan.fma.model.Album}
     * @return A new instance of fragment AlbumDetailFragment.
     */
    public static TrackDetailFragment newInstance(ArrayList<Item> tracks, int position) {
        TrackDetailFragment fragment = new TrackDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ITEM_PARCEL, tracks.get(position));
        args.putParcelableArrayList(KEY_ITEM_LIST, tracks);
        args.putInt(KEY_ITEM_POSITION, position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        mTracks = args.getParcelableArrayList(KEY_ITEM_LIST);
        mListPosition = args.getInt(KEY_ITEM_POSITION);

        mNetworkRequestListener = new NetworkRequestListener<Item>() {
            @Override
            public void onSuccess(@Nullable Item response, int statusCode) {
                if (response != null && statusCode == HttpURLConnection.HTTP_OK) {
                    Timber.d("Received response for track details: " + response);
                    setItem((Track) response);
                    showBasicView();
                    onItemLoaded(mItem);
                }
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                Timber.w(errorMessage);
                showErrorView();
            }
        };
    }

    @Override
    protected void showBasicView() {
        showContentView();
        mTitleView.setText(mItem.getTitle());
        FmaApplication.getInstance().getImageLoader().get(mItem.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response != null) {
                    Bitmap btm = response.getBitmap();
                    if (btm != null && mTrackImageView != null) {
                        mTrackImageView.setImageBitmap(btm);
                        mItem.setImageBitmap(btm);
                        BitmapWorkerTask task = new BitmapWorkerTask(mTrackImageContainer);
                        task.execute(btm);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.w("Can't load image bitmap url: " + mItem.getImage());
            }
        });
    }

    private boolean hasPrevious() {
        return mListPosition > 0;
    }

    private boolean hasNext() {
        return mListPosition < mTracks.size() - 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPaused) {
            mPaused = false;
        }
    }

    @Override
    public void onDestroy() {
        mActivity.unbindService(mServiceConnection);
        mMusicService = null;
        super.onDestroy();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };

    public int getCurrentPosition() {
        if (mMusicService != null && mIsBound)
            return mMusicService.getPosition();
        else return 0;
    }

    public int getDuration() {
        if (mMusicService != null && mIsBound && mPrepared)
            return mMusicService.getDuration();
        else return 0;
    }

    public boolean isPlaying() {
        return mMusicService != null && mIsBound && mMusicService.isPlaying();
    }

    public void pause() {
        mPlaybackPaused = true;
        mMusicService.pausePlayer();
    }

    public void seekTo(int position) {
        mMusicService.seek(position);
    }

    public void start() {
        mMusicService.startPlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(mActivity, MusicService.class);
            mActivity.bindService(mPlayIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mActivity.getWindow().setStatusBarColor(Color.parseColor(mItem.getGenres().get(0).getColor()));
//        }
        showContentView();
        init();
    }

    @Override
    public void refresh() {
        super.refresh();
        new NetworkManager().getTrackById(mNetworkRequestListener, mItem.getId());
    }

    private void init() {
        mButtonPlayPause.setOnClickListener(this);
        mProgress.setMax(99); // It means 100% .0-99
        mProgress.setPadding(0, 0, 0, 0);
        mProgress.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.progress_bar));
        mProgress.setOnTouchListener(this);
        mSongTime.setText(mItem.getDuration());
        updatePlayerPreviousNextButtonsUI();
    }

    private void updatePlayerPreviousNextButtonsUI() {
        mButtonPrevious.setEnabled(hasPrevious());
        mButtonNext.setEnabled(hasNext());
    }

    /**
     * Updates the SeekBar primary progress by current song playing position
     */
    private void updateProgress() {
        if (!mPrepared) {
            return;
        }
        if (mProgress != null) {
            int durationInMillis = getDuration();
            int timePercentage = durationInMillis != 0 ? getCurrentPosition() * 100 / durationInMillis : 0;
            mProgress.setProgress(timePercentage); // This gives a percentage of playing song length"
            mProgressPassed.setText(AppUtils.durationConverter(timePercentage * durationInMillis / 100000));
        }

        Runnable progressUpdater = this::updateProgress;
        handler.postDelayed(progressUpdater, 1000);
    }

    @OnClick(R.id.button_play_pause)
    public void onClick(View v) {
        if (!mPrepared) {
            return;
        }
        if (isPlaying()) {
            pause();
            mButtonPlayPause.setImageResource(R.drawable.ic_av_play);
        } else {
            start();
            mButtonPlayPause.setImageResource(R.drawable.ic_av_pause);
        }
    }

    @OnClick(R.id.button_download)
    public void onDownloadButtonClick() {
        Intent intent = new Intent(mActivity, MusicDownloaderService.class);
        intent.putExtra(MusicDownloaderService.KEY_TRACK_URL, mItem.getFileUrl());
        intent.putExtra(MusicDownloaderService.KEY_TRACK_NAME, mItem.getTitle());
        mActivity.startService(intent);
    }

    @OnClick(R.id.license)
    public void onLicenseViewClick() {
        if (mItem.getLicenseUrl() == null) {
            String trackTitle = mItem.getTitle();
            Timber.e(getString(R.string.empty_license_url, trackTitle));
            Toast.makeText(mActivity, getString(R.string.empty_license_url, trackTitle), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((mItem.getLicenseUrl())));
        startActivity(browserIntent);
    }

    @OnClick(R.id.button_previous)
    public void onPreviousButtonClick() {
        if (!hasPrevious()) {
            return;
        }
        pause();
        if (mTracks != null) {
            mListPosition--;
            setItem(mTracks.get(mListPosition));
        }

        updatePlayerPreviousNextButtonsUI();
        mProgressView.startAnimation();
        mProgressView.setVisibility(View.VISIBLE);
        refresh();
    }

    @OnClick(R.id.button_next)
    public void onNextButtonClick() {
        if (!hasNext()) {
            Toast.makeText(mActivity, getString(R.string.no_more_tracks_to_load, mItem.getAlbumTitle()), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mTracks != null) {
            mListPosition++;
            setItem(mTracks.get(mListPosition));
        }

        updatePlayerPreviousNextButtonsUI();
        mProgressView.startAnimation();
        mProgressView.setVisibility(View.VISIBLE);
        refresh();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.seek_bar) {
            // SeekBar onTouch event handler. Seeks MediaPlayer to seekBar primary progress position
            if (isPlaying()) {
                SeekBar seekBar = (SeekBar) v;
                int playPositionInMillis = (mDurationInMillis / 100) * seekBar.getProgress();
                seekTo(playPositionInMillis);
            }
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    @OnClick(R.id.button_volume)
    public void updateVolumeMode() {
        mMute = !mMute;
        mButtonVolume.setActivated(mMute);
        mute(mMute);
    }

    private void mute(boolean mute) {
        AudioManager audioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, mute ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
            }
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, mute);
        }
    }

    @Override
    public void onItemLoaded(Item item) {
        Track track = (Track) item;
        track.setImageBitmap(mItem.getImageBitmap());
        mMusicService.setTrack(track);
        mMusicService.playTrack();
        mSongTime.setText(track.getDuration());
        mProgressView.startAnimation();
        mProgressView.setVisibility(View.VISIBLE);
        mLicense.bringToFront();

        setListeners();
        mPlaybackPaused = !mPlaybackPaused;
    }

    private void setListeners() {
        mMusicService.setPreparedListener(this);
        mMusicService.setStateChangeListener(this);
        mMusicService.setLoadNeighborTrackListener(this);
    }

    @Override
    public void onPlayerPrepared() {
        mProgressView.stopAnimation();
        mProgressView.setVisibility(View.GONE);

        mDurationInMillis = getDuration(); // gets the track length in milliseconds
        mPrepared = true;
        updatePlayerControlsUI(true);
        updateProgress();
    }

    @Override
    public void onPlayerStateChanged(boolean isPlaying) {
        updatePlayerControlsUI(isPlaying);
        updateProgress();
    }

    private void updatePlayerControlsUI(boolean isPlaying) {
        if (isPlaying) {
            mButtonPlayPause.setImageResource(R.drawable.ic_av_pause);
        } else {
            mButtonPlayPause.setImageResource(R.drawable.ic_av_play);
        }
    }

    @Override
    public void loadNeighborTrack(@AdjacentMode int mode) {
        switch (mode) {
            case NEXT:
                onNextButtonClick();
                break;
            case PREVIOUS:
                onPreviousButtonClick();
                break;
        }
    }

    @Override
    public void onBeforeDestroyView() {
        super.onBeforeDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        mute(false); // un-mute audio stream

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = mActivity.getWindow();
//            window.setStatusBarColor(ContextCompat.getColor(mActivity, R.color.colorPrimaryDark));
//        }
    }

    class BitmapWorkerTask extends AsyncTask<Bitmap, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private Bitmap source;

        BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        // Blur image in background.
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            source = params[0];
            return BitmapUtils.fastBlur(source, 0.85f, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}

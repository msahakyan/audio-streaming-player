package com.android.msahakyan.fma.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.msahakyan.fma.R;

import org.json.JSONException;

import io.branch.referral.Branch;
import timber.log.Timber;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            splashPlayer();
        } catch (Exception ex) {
            startControllerActivity();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public void splashPlayer() {
        setContentView(R.layout.activity_splash_screen);

        final VideoView videoView = (VideoView) findViewById(R.id.video_view);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
            + R.raw.splash_movie);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(mp -> startControllerActivity());
        videoView.setMediaController(null);
        videoView.start();
        videoView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    ((VideoView) v).stopPlayback();
                    clearListener((VideoView) v);
                    startControllerActivity();
                    break;
                default:
                    break;
            }
            return true;
        });

        ImageView imageView = (ImageView) findViewById(R.id.splash_logo_view);
        imageView.postDelayed(() -> {
            if (imageView != null) {
                imageView.bringToFront();
                imageView.postDelayed(() -> {
                    if (imageView != null) {
                        imageView.setVisibility(View.GONE);
                    }
                }, 4600);
            }
        }, 400);
    }

    private void clearListener(VideoView videoView) {
        videoView.setOnCompletionListener(null);
    }

    private synchronized void startControllerActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        branch.initSession((referringParams, error) -> {
            if (error == null) {
                // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                // params will be empty if no data found
                // ... insert custom logic here ...

                try {
                    if (referringParams.getBoolean("+clicked_branch_link")) {
                        new Handler(getMainLooper()).post(() ->
                            Toast.makeText(SplashScreenActivity.this, R.string.started_from_branch_deeplink, Toast.LENGTH_SHORT).show());
                    }
                } catch (JSONException e) {
                    Timber.e(e, "Can't parse reffered params json");
                }
            } else {
                Timber.e(error.getMessage());
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}

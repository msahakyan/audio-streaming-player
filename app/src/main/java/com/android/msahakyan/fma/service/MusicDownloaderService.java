package com.android.msahakyan.fma.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.android.msahakyan.fma.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static android.os.Environment.DIRECTORY_MUSIC;

/**
 * Created by msahakyan on 29/09/16.
 */

public class MusicDownloaderService extends IntentService {

    public static final String KEY_TRACK_URL = "KEY_TRACK_URL";
    public static final String KEY_TRACK_NAME = "KEY_TRACK_NAME";
    private static final String CONTENT_TYPE_MP3 = ".mp3";
    private static final String APP_FOLDER = "/free_music";

    /**
     * Creates an IntentService.
     */
    public MusicDownloaderService() {
        super("Track downloader service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String trackUrl = intent.getStringExtra(KEY_TRACK_URL);
            String trackName = intent.getStringExtra(KEY_TRACK_NAME);
            try {
                downloadTrack(trackUrl, trackName);
            } catch (IOException e) {
                Timber.e(e, "Can't download track with url: " + trackUrl);
            }
        }
    }

    private void downloadTrack(String trackUrl, String trackName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(trackUrl)
            .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            InputStream inputStream = response.body().byteStream();
            saveTrackToLocalStorage(inputStream, trackName);
        } else {
            Timber.w(response.code() + " : " + response.message());
            showToast(response.message());
        }

        response.body().close();
    }

    private void saveTrackToLocalStorage(InputStream is, String trackName) {
        if (!isExternalStorageWritable()) {
            showToast(getString(R.string.no_permition_for_write_to_external_storage));
            return;
        }

        File root = android.os.Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC);

        File dir = new File(root.getAbsolutePath() + APP_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, trackName + CONTENT_TYPE_MP3);

        if (file.exists()) {
            showToast(getString(R.string.file_already_exists, file.getName()));
            return;
        }
        showToast(getString(R.string.download_in_process));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            showToast(getString(R.string.track_save_success, file.getName()));
        } catch (IOException e) {
            Timber.e(e, "Can't save file: " + file.getName() + " to ");
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Checks if external storage is available for read and write
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}

package com.android.msahakyan.fma.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.msahakyan.fma.R;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Created by msahakyan on 26/07/16.
 */

public class AppUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static void showCustomDialog(final Context context, String textContent) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);

        final ViewGroup content = (ViewGroup) layout.findViewById(R.id.dialog_content);
        content.setVisibility(View.VISIBLE);
        content.addView(newText(context, textContent));

        new AlertDialog.Builder(context)
            .setView(layout)
            .setNeutralButton("Ok", (dialog, which) -> {
                dialog.dismiss();
            })
            .setCancelable(true)
            .setInverseBackgroundForced(true)
            .create()
            .show();
    }

    private static View newText(Context context, String text) {
        TextView view = new TextView(context);
        view.setText(Html.fromHtml(text));
        view.setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1);

        return view;
    }

    public static String durationConverter(long totalSecs) {
        int minutes = (int) (totalSecs % 3600) / 60;
        int seconds = (int) totalSecs % 60;

        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }

    public static String getCreationDateOnly(String fullDate) {
        if (fullDate == null) {
            return null;
        }
        int spaceLocation = fullDate.indexOf(" ");
        return fullDate.substring(0, spaceLocation);
    }

    public static boolean isJellyBeanOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static void setCollectionQualifier(Collection<Item> collection, String qualifier) {
        if (collection == null) {
            return;
        }
        
        for (Item item : collection) {
            item.setQualifier(qualifier);
        }
    }
}

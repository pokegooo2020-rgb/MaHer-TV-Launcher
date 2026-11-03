package com.maher.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private int white = Color.WHITE;
    private int muted = Color.rgb(185, 195, 215);
    private LinearLayout appList;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        buildScreen();
    }

    private void buildScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(56, 34, 56, 30);
        root.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,
            new int[]{Color.rgb(12, 20, 42), Color.rgb(28, 38, 72), Color.rgb(8, 13, 28)}));

        TextView brand = new TextView(this);
        brand.setText("MaHer TV");
        brand.setTextColor(white);
        brand.setTextSize(34);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(brand);

        TextView subtitle = new TextView(this);
        subtitle.setText("TU ENTRETENIMIENTO, A TU MANERA");
        subtitle.setTextColor(muted);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, 4, 0, 18);
        root.addView(subtitle);

        TextView clock = new TextView(this);
        clock.setText(new SimpleDateFormat("EEEE, d MMMM  •  HH:mm", new Locale("es", "MX")).format(new Date()));
        clock.setTextColor(white);
        clock.setTextSize(21);
        root.addView(clock);

        TextView section = new TextView(this);
        section.setText("TUS APLICACIONES");
        section.setTextColor(muted);
        section.setTextSize(14);
        section.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        section.setPadding(0, 28, 0, 12);
        root.addView(section);

        ScrollView scroll = new ScrollView(this);
        appList = new LinearLayout(this);
        appList.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(appList);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
        loadApps();
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent query = new Intent(Intent.ACTION_MAIN, null);
        query.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(query, 0);
        if (apps == null || apps.isEmpty()) {
            query.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            query.addCategory(Intent.CATEGORY_LAUNCHER);
            apps = pm.queryIntentActivities(query, 0);
        }
        for (ResolveInfo info : apps) {
            final String pkg = info.activityInfo.packageName;
            TextView item = new TextView(this);
            item.setText("▶   " + info.loadLabel(pm));
            item.setTextColor(white);
            item.setTextSize(20);
            item.setFocusable(true);
            item.setFocusableInTouchMode(true);0
            item.setPadding(22, 17, 22, 17);
            GradientDrawable bg = new GradientDrawable();
            bg.setColor(Color.argb(55, 255, 255, 255));
            bg.setCornerRadius(18);
            item.setBackground(bg);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, 0, 0, 10);
            appList.addView(item, lp);
            item.setOnClickListener(v -> {
                Intent launch = pm.getLaunchIntentForPackage(pkg);
                if (launch != null) startActivity(launch);
            });
            item.setOnFocusChangeListener((v, focused) -> {
                v.setAlpha(focused ? 1f : 0.82f);
                v.setScaleX(focused ? 1.02f : 1f);
                v.setScaleY(focused ? 1.02f : 1f);
            });
        }
        if (apps == null || apps.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No se encontraron aplicaciones compatibles.");
            empty.setTextColor(muted);
            appList.addView(empty);
        }
    }
}

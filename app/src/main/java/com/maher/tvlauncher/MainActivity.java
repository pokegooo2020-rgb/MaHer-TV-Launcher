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
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.LinkedHashMap;

public class MainActivity extends Activity {

    private final int white = Color.WHITE;
    private final int muted = Color.rgb(190, 200, 225);
    private LinearLayout appList;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        buildScreen();
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(radius);
        return bg;
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    private void buildScreen() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(55, 30, 45, 25);

        root.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{
                Color.rgb(8, 13, 35),
                Color.rgb(31, 25, 75),
                Color.rgb(10, 18, 43)
            }
        ));

        TextView brand = text("MaHer TV", 38, white);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(brand);

        TextView subtitle = text(
            "TU ENTRETENIMIENTO, A TU MANERA",
            13, muted
        );
        subtitle.setPadding(2, 5, 0, 8);
        root.addView(subtitle);

        TextView clock = text(
            new SimpleDateFormat(
                "EEEE, d MMMM   •   HH:mm",
                new Locale("es", "MX")
            ).format(new Date()),
            19, white
        );
        root.addView(clock);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(25, 15, 25, 15);
        hero.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            new int[]{
                Color.rgb(35, 67, 145),
                Color.rgb(91, 48, 150),
                Color.rgb(35, 38, 90)
            }
        ));
        hero.setCornerRadius(25);

        TextView welcome = text("BIENVENIDO A MAHER TV", 23, white);
        welcome.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        hero.addView(welcome);

        TextView description = text(
            "Todo tu entretenimiento en un solo lugar",
            15, white
        );
        description.setPadding(0, 8, 0, 0);
        hero.addView(description);

        LinearLayout.LayoutParams heroParams =
            new LinearLayout.LayoutParams(-1, 125);
        heroParams.setMargins(0, 22, 0, 20);
        root.addView(hero, heroParams);

        TextView section = text("TUS APLICACIONES", 19, white);
        section.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        section.setPadding(0, 8, 0, 14);
        root.addView(section);

        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);

        appList = new LinearLayout(this);
        appList.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(appList);

        root.addView(scroll,
            new LinearLayout.LayoutParams(-1, 165));

        TextView footer = text(
            "MAHER TV  •  DISFRUTA TU ENTRETENIMIENTO",
            12, muted
        );
        footer.setPadding(0, 20, 0, 0);
        root.addView(footer);

        setContentView(root);
        loadApps();
    }

    private void loadApps() {

        PackageManager pm = getPackageManager();

        LinkedHashMap<String, ResolveInfo> found =
            new LinkedHashMap<>();

        String[] categories = {
            Intent.CATEGORY_LEANBACK_LAUNCHER,
            Intent.CATEGORY_LAUNCHER
        };

        for (String category : categories) {
            Intent query = new Intent(Intent.ACTION_MAIN);
            query.addCategory(category);

            List<ResolveInfo> apps =
                pm.queryIntentActivities(query, 0);

            for (ResolveInfo info : apps) {
                if (info.activityInfo != null) {
                    String key =
                        info.activityInfo.packageName + "/" +
                        info.activityInfo.name;
                    found.put(key, info);
                }
            }
        }

        for (ResolveInfo info : found.values()) {

            final String pkg = info.activityInfo.packageName;
            final String activityName = info.activityInfo.name;

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setPadding(12, 12, 12, 12);
            card.setBackground(background(
                Color.rgb(36, 48, 86), 20
            ));
            card.setFocusable(true);
            card.setClickable(true);

            ImageView icon = new ImageView(this);
            try {
                icon.setImageDrawable(info.loadIcon(pm));
            } catch (Exception ignored) {}

            card.addView(icon,
                new LinearLayout.LayoutParams(65, 65));

            TextView label = text(
                info.loadLabel(pm).toString(),
                14, white
            );
            label.setGravity(Gravity.CENTER);
            label.setMaxLines(2);
            label.setPadding(0, 10, 0, 0);

            card.addView(label);

            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(125, 145);
            params.setMargins(0, 0, 15, 0);
            appList.addView(card, params);

            card.setOnFocusChangeListener((v, focused) -> {
                v.setScaleX(focused ? 1.10f : 1f);
                v.setScaleY(focused ? 1.10f : 1f);
                v.setAlpha(focused ? 1f : 0.85f);
            });

            card.setOnClickListener(v -> {
                try {
                    Intent launch = new Intent(Intent.ACTION_MAIN);
                    launch.addCategory(
                        Intent.CATEGORY_LAUNCHER
                    );
                    launch.setClassName(pkg, activityName);
                    launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launch);
                } catch (Exception e) {
                    try {
                        Intent launch =
                            pm.getLaunchIntentForPackage(pkg);
                        if (launch != null) {
                            startActivity(launch);
                        }
                    } catch (Exception ignored) {}
                }
            });
        }

        if (found.isEmpty()) {
            TextView empty = text(
                "No se encontraron aplicaciones.",
                17, muted
            );
            appList.addView(empty);
        }
    }
}

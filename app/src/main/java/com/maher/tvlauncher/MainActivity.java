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
    private final int muted = Color.rgb(220, 220, 220);
    private final int red = Color.rgb(190, 20, 45);
    private LinearLayout content;
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

    private GradientDrawable panel(int color, int radius) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(radius);
        bg.setStroke(2, Color.rgb(90, 90, 100));
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
        root.setOrientation(LinearLayout.HORIZONTAL);
        android.graphics.drawable.Drawable fondo =
    android.graphics.drawable.Drawable.createFromStream(
        getResources().openRawResource(
            getResources().getIdentifier(
                "fondo_chivas", "drawable", getPackageName()
            )
        ), null
    );

root.setBackground(fondo);

        LinearLayout menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.VERTICAL);
        menu.setPadding(22, 30, 18, 20);
        menu.setBackgroundColor(Color.rgb(12, 13, 20));

        TextView logo = text("MAHER", 27, white);
        logo.setTypeface(null, Typeface.BOLD);
        menu.addView(logo);

        TextView team = text("TV  •  CHIVAS", 13, red);
        team.setTypeface(null, Typeface.BOLD);
        team.setPadding(0, 3, 0, 35);
        menu.addView(team);

        String[] options = {
            "⌂   INICIO",
            "▦   APLICACIONES",
            "▶   PELÍCULAS",
            "▣   SERIES",
            "⚙   AJUSTES"
        };

        for (int i = 0; i < options.length; i++) {
            final int index = i;
            TextView item = text(options[i], 15, white);
            item.setPadding(12, 17, 8, 17);
            item.setFocusable(true);
            item.setClickable(true);
            item.setBackground(panel(
                i == 0 ? red : Color.rgb(30, 31, 40), 12
            ));

            LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, -2);
            p.setMargins(0, 0, 0, 12);
            menu.addView(item, p);

            item.setOnFocusChangeListener((v, focused) -> {
                v.setBackground(panel(
                    focused || index == 0
                    ? red : Color.rgb(30, 31, 40), 12
                ));
            });

            item.setOnClickListener(v -> {
                if (index == 1) {
                    content.removeAllViews();
                    showApplications();
                } else if (index == 0) {
                    content.removeAllViews();
                    showHome();
                } else {
                    content.removeAllViews();
                    TextView message = text(
                        index == 2 ? "PELÍCULAS" :
                        index == 3 ? "SERIES" : "AJUSTES",
                        28, white
                    );
                    message.setPadding(20, 30, 0, 0);
                    content.addView(message);
                }
            });
        }

        TextView bottom = text("¡VAMOS CHIVAS!", 13, white);
        bottom.setTypeface(null, Typeface.BOLD);
        bottom.setPadding(0, 30, 0, 0);
        menu.addView(bottom);

        root.addView(menu,
            new LinearLayout.LayoutParams(205, -1));

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(30, 25, 30, 20);

        root.addView(content,
            new LinearLayout.LayoutParams(0, -1, 1));

        setContentView(root);
        showHome();
    }

    private void showHome() {

        TextView clock = text(
            new SimpleDateFormat(
                "EEEE, d MMMM   •   HH:mm",
                new Locale("es", "MX")
            ).format(new Date()),
            17, white
        );
        content.addView(clock);

        TextView title = text("BIENVENIDO A MAHER TV", 30, white);
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(0, 25, 0, 5);
        content.addView(title);

        TextView subtitle = text(
            "EL ORGULLO DE GUADALAJARA",
            17, Color.rgb(255, 80, 100)
        );
        subtitle.setTypeface(null, Typeface.BOLD);
        content.addView(subtitle);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(25, 20, 25, 20);
        hero.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            new int[]{
                Color.rgb(175, 15, 40),
                Color.rgb(70, 10, 25),
                Color.rgb(20, 30, 55)
            }
        ));

        TextView heroTitle = text(
            "¡ARRIBA LAS CHIVAS!", 27, white
        );
        heroTitle.setTypeface(null, Typeface.BOLD);
        hero.addView(heroTitle);

        TextView heroText = text(
            "Pasión, historia y orgullo rojiblanco",
            16, white
        );
        heroText.setPadding(0, 10, 0, 0);
        hero.addView(heroText);

        LinearLayout.LayoutParams hp =
            new LinearLayout.LayoutParams(-1, 135);
        hp.setMargins(0, 22, 0, 22);
        content.addView(hero, hp);

        TextView appsTitle = text(
            "TUS APLICACIONES", 21, white
        );
        appsTitle.setTypeface(null, Typeface.BOLD);
        appsTitle.setPadding(0, 5, 0, 12);
        content.addView(appsTitle);

        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);

        appList = new LinearLayout(this);
        appList.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(appList);

        content.addView(scroll,
            new LinearLayout.LayoutParams(-1, 195));

        loadApps();

        TextView footer = text(
            "MAHER TV  •  UNA PASIÓN INCOMPARABLE",
            13, muted
        );
        footer.setPadding(0, 20, 0, 0);
        content.addView(footer);
    }

    private void showApplications() {
        TextView heading = text(
            "TODAS TUS APLICACIONES", 25, white
        );
        heading.setTypeface(null, Typeface.BOLD);
        heading.setPadding(0, 0, 0, 20);
        content.addView(heading);

        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);

        appList = new LinearLayout(this);
        appList.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(appList);

        content.addView(scroll);
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
            card.setPadding(18, 18, 18, 18);
            card.setBackground(panel(
                Color.rgb(45, 45, 55), 18
            ));
            card.setFocusable(true);
            card.setClickable(true);

            ImageView icon = new ImageView(this);
            try {
                icon.setImageDrawable(info.loadIcon(pm));
            } catch (Exception ignored) {}

            card.addView(icon,
    new LinearLayout.LayoutParams(90, 90));

            TextView label = text(
                info.loadLabel(pm).toString(), 14, white
            );
            label.setGravity(Gravity.CENTER);
            label.setMaxLines(2);
            label.setPadding(0, 8, 0, 0);
            card.addView(label);

            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(175, 165);
            params.setMargins(0, 0, 15, 0);
            appList.addView(card, params);

            card.setOnFocusChangeListener((v, focused) -> {
                v.setScaleX(focused ? 1.10f : 1f);
                v.setScaleY(focused ? 1.10f : 1f);
                v.setBackground(panel(
                    focused ? red : Color.rgb(45, 45, 55), 18
                ));
            });

            card.setOnClickListener(v -> {
                try {
                    Intent launch = new Intent(Intent.ACTION_MAIN);
                    launch.addCategory(Intent.CATEGORY_LAUNCHER);
                    launch.setClassName(pkg, activityName);
                    startActivity(launch);
                } catch (Exception e) {
                    try {
                        Intent launch =
                            pm.getLaunchIntentForPackage(pkg);
                        if (launch != null) startActivity(launch);
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

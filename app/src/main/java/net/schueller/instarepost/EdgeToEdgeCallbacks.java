package net.schueller.instarepost;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Android 15+ draws apps edge-to-edge. Pads every activity's content view by the system bar,
 * display cutout and keyboard insets so nothing is drawn under the status / navigation bars.
 * Register once from Application.onCreate().
 */
public final class EdgeToEdgeCallbacks implements Application.ActivityLifecycleCallbacks {

    public static void register(Application app) {
        app.registerActivityLifecycleCallbacks(new EdgeToEdgeCallbacks());
    }

    static void apply(View content) {
        if (content == null || content.getTag(R.id.edge_to_edge_applied) != null) {
            return;
        }
        content.setTag(R.id.edge_to_edge_applied, Boolean.TRUE);
        ViewCompat.setOnApplyWindowInsetsListener(content, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, Math.max(bars.bottom, ime.bottom));
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.requestApplyInsets(content);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // Leave third-party (ads / consent) activities alone; they handle insets themselves.
        if (activity.getClass().getName().startsWith("com.google.")) {
            return;
        }
        apply(activity.findViewById(android.R.id.content));
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}

package com.schulte.table.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class VibrationHelper {

    public static void vibrateCorrect(Context ctx) {
        vibrate(ctx, 30);
    }

    public static void vibrateWrong(Context ctx) {
        vibrate(ctx, 100);
    }

    public static void vibrateComplete(Context ctx) {
        Vibrator vibrator = getVibrator(ctx);
        if (vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, 128));
        } else {
            vibrator.vibrate(50);
        }
    }

    private static void vibrate(Context ctx, long millis) {
        Vibrator vibrator = getVibrator(ctx);
        if (vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millis, 100));
        } else {
            vibrator.vibrate(millis);
        }
    }

    @SuppressWarnings("deprecation")
    private static Vibrator getVibrator(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vm = (VibratorManager) ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            return vm != null ? vm.getDefaultVibrator() : null;
        } else {
            return (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }
}

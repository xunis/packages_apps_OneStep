package com.smartisanos.sidebar.util;

import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.UserHandle;

public class LaunchApp {
    private static final LOG log = LOG.getInstance(LaunchApp.class);

    public static final String EXTRA_HAD_CHOOSE = "com.smartisanos.doppelganger.had_choose";

    public static void start(Context context, Intent intent) {
        start(context, intent, false, null, 0);
    }

    public static void start(Context context, Intent intent, boolean checkDoppelganger, String pkg, int userId) {
        if (context == null) {
            return;
        }
        if (intent == null) {
            return;
        }
        boolean isDoppelganger = false;
        if (checkDoppelganger) {
            String packageName = pkg;
            if (packageName == null) {
                packageName = intent.getPackage();
            }
            isDoppelganger = isAppInDoppelgangerStatus(context, packageName);
        }
        try {
            if (!isDoppelganger) {
                context.startActivity(intent);
            } else {
                intent.putExtra(EXTRA_HAD_CHOOSE, true);
                //UserHandle.USER_DOPPELGANGER
                context.startActivityAsUser(intent, null, new UserHandle(userId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isAppInDoppelgangerStatus(Context context, String pkg) {
        if (pkg != null && context != null) {
            try {
                PackageInfo info = AppGlobals.getPackageManager().getPackageInfo(pkg, 0, UserHandle.USER_DOPPELGANGER);
                if (info != null) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
package com.topjohnwu.magisk.components;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.topjohnwu.magisk.Global;
import com.topjohnwu.magisk.MagiskManager;
import com.topjohnwu.magisk.R;
import com.topjohnwu.magisk.asyncs.MarkDownWindow;
import com.topjohnwu.magisk.receivers.ManagerUpdate;
import com.topjohnwu.magisk.utils.Const;
import com.topjohnwu.magisk.utils.Utils;

public class ManagerInstallDialog extends CustomAlertDialog {

    public ManagerInstallDialog(@NonNull Activity activity) {
        super(activity);
        MagiskManager mm = Utils.getMagiskManager(activity);
        String filename = Utils.fmt("MagiskManager-v%s(%d).apk",
                Global.remoteManagerVersionString, Global.remoteManagerVersionCode);
        setTitle(mm.getString(R.string.repo_install_title, mm.getString(R.string.app_name)));
        setMessage(mm.getString(R.string.repo_install_msg, filename));
        setCancelable(true);
        setPositiveButton(R.string.install, (d, i) -> activity.runWithPermission(
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, () -> {
                    Intent intent = new Intent(mm, ManagerUpdate.class);
                    intent.putExtra(Const.Key.INTENT_SET_LINK, Global.managerLink);
                    intent.putExtra(Const.Key.INTENT_SET_FILENAME, filename);
                    mm.sendBroadcast(intent);
                }))
                .setNegativeButton(R.string.no_thanks, null);
        if (!TextUtils.isEmpty(Global.managerNoteLink)) {
            setNeutralButton(R.string.app_changelog, (d, i) ->
                    new MarkDownWindow(activity, null, Global.managerNoteLink).exec());
        }
    }
}

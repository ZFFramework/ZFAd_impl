package com.ZFFramework.ZFAd_impl;

import com.ZFFramework.NativeUtil.ZFAndroidPost;
import com.ZFFramework.NativeUtil.ZFRunnable;
import com.ZFFramework.NativeUtil.ZFString;
import com.ZFFramework.NativeUtil.ZFTaskId;
import com.ZFFramework.ZF_impl.ZFMainEntry;
import com.qq.e.comm.managers.GDTAdSdk;

import java.util.List;

public class ZFAd {

    public static int appIdUpdate(String appId, ZFRunnable.P2<Boolean, String> callback) {
        if ((_initRunning || _initSuccess) && !ZFString.isEqual(_appId, appId)) {
            ZFRunnable.RUN(callback, false, String.format("[GDT] registering a different appId: %s => %s", _appId, appId));
            return ZFTaskId.INVALID;
        }
        if (!_initRunning && _initSuccess) {
            ZFRunnable.RUN(callback, true, null);
            return ZFTaskId.INVALID;
        }
        if (!_initRunning) {
            if (ZFString.isEmpty(appId)) {
                ZFRunnable.RUN(callback, false, "[GDT] null appId");
                return ZFTaskId.INVALID;
            }
            _initRunning = true;
            _initSuccess = false;
            _appId = appId;
            GDTAdSdk.initWithoutStart(ZFMainEntry.appContext(), appId);
            GDTAdSdk.start(new GDTAdSdk.OnStartListener() {
                @Override
                public void onStartSuccess() {
                    _notifyFinish(true, null);
                }

                @Override
                public void onStartFailed(Exception e) {
                    _notifyFinish(false, String.format("[GDT] %s appId setup fail: %s", appId, e));
                }
            });
        }
        return _taskMap.obtain(callback);
    }

    public static void appIdUpdateCancel(int taskId) {
        _taskMap.release(taskId);
    }

    private static String _appId = null;
    private static boolean _initRunning = false;
    private static boolean _initSuccess = false;
    private static final ZFTaskId<ZFRunnable.P2<Boolean, String>> _taskMap = new ZFTaskId<>();

    private static void _notifyFinish(boolean success, String errorHint) {
        ZFAndroidPost.run(new Runnable() {
            @Override
            public void run() {
                _initRunning = false;
                _initSuccess = success;
                List<ZFRunnable.P2<Boolean, String>> tasks = _taskMap.releaseAll();
                for (ZFRunnable.P2<Boolean, String> task : tasks) {
                    task.run(success, errorHint);
                }
            }
        });
    }

}


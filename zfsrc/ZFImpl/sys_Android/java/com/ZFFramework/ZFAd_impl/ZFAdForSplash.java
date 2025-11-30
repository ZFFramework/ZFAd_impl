package com.ZFFramework.ZFAd_impl;

import com.ZFFramework.NativeUtil.ZFRunnable;
import com.ZFFramework.NativeUtil.ZFString;
import com.ZFFramework.NativeUtil.ZFTaskId;
import com.ZFFramework.ZFUIKit_impl.ZFUIRootWindow;
import com.ZFFramework.ZF_impl.ZFResultType;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.lang.ref.WeakReference;

public class ZFAdForSplash {

    public long zfjniPointerOwnerZFAd = -1;
    public SplashAD impl = null;

    public Object zfnativeImpl() {
        return impl;
    }

    public static Object native_nativeAdCreate(long zfjniPointerOwnerZFAd) {
        ZFAdForSplash nativeAd = new ZFAdForSplash();
        nativeAd.zfjniPointerOwnerZFAd = zfjniPointerOwnerZFAd;
        return nativeAd;
    }

    public static void native_nativeAdDestroy(Object nativeAd) {
        ZFAdForSplash nativeAdTmp = (ZFAdForSplash) nativeAd;
        nativeAdTmp.zfjniPointerOwnerZFAd = -1;
        nativeAdTmp._nativeAdStarted = false;
        nativeAdTmp.impl = null;
    }

    public static void native_nativeAdUpdate(
            Object nativeAd
            , String appId
            , String adId
    ) {
        ZFAdForSplash nativeAdTmp = (ZFAdForSplash) nativeAd;
        if (nativeAdTmp._appIdUpdateTaskId != ZFTaskId.INVALID) {
            ZFAd.appIdUpdateCancel(nativeAdTmp._appIdUpdateTaskId);
        }
        nativeAdTmp._adId = null;
        nativeAdTmp._appIdUpdateTaskId = ZFAd.appIdUpdate(appId, new ZFRunnable.P2<Boolean, String>() {
            @Override
            public void run(Boolean success, String errorHint) {
                nativeAdTmp._appIdUpdateTaskId = ZFTaskId.INVALID;
                nativeAdTmp._adId = adId;
                nativeAdTmp._update();
            }
        });
    }

    public static void native_nativeAdStart(Object nativeAd, Object window) {
        ZFAdForSplash nativeAdTmp = (ZFAdForSplash) nativeAd;
        nativeAdTmp._nativeAdStarted = true;
        nativeAdTmp._nativeAdFinished = false;
        nativeAdTmp._window = new WeakReference<>((ZFUIRootWindow) window);
        nativeAdTmp._update();
    }

    public static void native_nativeAdStop(Object nativeAd) {
        ZFAdForSplash nativeAdTmp = (ZFAdForSplash) nativeAd;
        nativeAdTmp._nativeAdStarted = false;
        nativeAdTmp._window = null;
    }

    // ============================================================
    public static native void native_notifyAdOnError(long zfjniPointerOwnerZFAd, String errorHint);

    public static native void native_notifyAdOnDisplay(long zfjniPointerOwnerZFAd);

    public static native void native_notifyAdOnClick(long zfjniPointerOwnerZFAd);

    public static native void native_notifyAdOnTimer(long zfjniPointerOwnerZFAd, long remainingTimeClick);

    public static native void native_notifyAdOnStop(long zfjniPointerOwnerZFAd, int resultType);

    // ============================================================
    private int _appIdUpdateTaskId = ZFTaskId.INVALID;
    private String _adId = null;
    private boolean _nativeAdStarted = false;
    private boolean _nativeAdLoaded = false;
    private boolean _nativeAdFinished = false;
    private WeakReference<ZFUIRootWindow> _window = null;

    private final SplashADListener _implListener = new SplashADListener() {
        @Override
        public void onADDismissed() {
            if (zfjniPointerOwnerZFAd != -1) {
                _nativeAdStarted = false;
                _window = null;
                native_notifyAdOnStop(zfjniPointerOwnerZFAd, _nativeAdFinished ? ZFResultType.e_Success : ZFResultType.e_Cancel);
            }
        }

        @Override
        public void onNoAD(AdError error) {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnError(zfjniPointerOwnerZFAd, error != null ? error.getErrorMsg() : "[GDT] onNoAD");
            }
        }

        @Override
        public void onADPresent() {
        }

        @Override
        public void onADClicked() {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnClick(zfjniPointerOwnerZFAd);
            }
        }

        @Override
        public void onADTick(long l) {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnTimer(zfjniPointerOwnerZFAd, l);
            }
        }

        @Override
        public void onADExposure() {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnDisplay(zfjniPointerOwnerZFAd);
            }
        }

        @Override
        public void onADLoaded(long l) {
            if (zfjniPointerOwnerZFAd != -1) {
                _nativeAdLoaded = true;
                _update();
            }
        }
    };

    private void _update() {
        if (!_nativeAdStarted || ZFString.isEmpty(_adId)) {
            return;
        }
        if (_window == null || _window.get() == null) {
            native_notifyAdOnError(zfjniPointerOwnerZFAd, "[GDT] unable to obtain window");
            return;
        }

        if (impl == null) {
            _nativeAdLoaded = false;
            impl = new SplashAD(
                    _window.get()
                    , _adId
                    , _implListener
                    , 0
            );
        }
        if (_nativeAdLoaded) {
            impl.showFullScreenAd(_window.get().rootContainer());
        } else {
            impl.fetchFullScreenAdOnly();
        }
    }

}


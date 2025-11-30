package com.ZFFramework.ZFAd_impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ZFFramework.NativeUtil.ZFAndroidLog;
import com.ZFFramework.NativeUtil.ZFRunnable;
import com.ZFFramework.NativeUtil.ZFString;
import com.ZFFramework.NativeUtil.ZFTaskId;
import com.ZFFramework.ZFUIKit_impl.ZFUIRootWindow;
import com.ZFFramework.ZF_impl.ZFMainEntry;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

import java.lang.ref.WeakReference;

public class ZFAdForBanner extends FrameLayout {

    public long zfjniPointerOwnerZFAd = -1;
    public UnifiedBannerView impl = null;

    public Object zfnativeImpl() {
        return impl;
    }

    public static Object native_nativeAdCreate(long zfjniPointerOwnerZFAd) {
        ZFAdForBanner nativeAd = new ZFAdForBanner(ZFMainEntry.appContext());
        nativeAd.zfjniPointerOwnerZFAd = zfjniPointerOwnerZFAd;
        return nativeAd;
    }

    public static void native_nativeAdDestroy(Object nativeAd) {
        ZFAdForBanner nativeAdTmp = (ZFAdForBanner) nativeAd;
        nativeAdTmp.zfjniPointerOwnerZFAd = -1;
        nativeAdTmp.impl = null;
    }

    public static void native_nativeAdUpdate(
            Object nativeAd
            , String appId
            , String adId
    ) {
        ZFAdForBanner nativeAdTmp = (ZFAdForBanner) nativeAd;
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

    public static void native_nativeAdWindowUpdate(Object nativeAd, Object window) {
        if (window == null) {
            ZFAndroidLog.shouldNotGoHere();
            return;
        }
        ZFAdForBanner nativeAdTmp = (ZFAdForBanner) nativeAd;
        if (nativeAdTmp._window != null && nativeAdTmp._window.get() != null && nativeAdTmp._window.get() != window) {
            ZFAndroidLog.shouldNotGoHere();
            return;
        }
        nativeAdTmp._window = new WeakReference<>((ZFUIRootWindow) window);
        nativeAdTmp._update();
    }

    // ============================================================
    public static native void native_notifyAdOnError(long zfjniPointerOwnerZFAd, String errorHint);

    public static native void native_notifyAdOnDisplay(long zfjniPointerOwnerZFAd);

    public static native void native_notifyAdOnClick(long zfjniPointerOwnerZFAd);

    public static native void native_notifyAdOnClose(long zfjniPointerOwnerZFAd);

    // ============================================================
    public ZFAdForBanner(Context context) {
        super(context);
    }

    public ZFAdForBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZFAdForBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ZFAdForBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // ============================================================
    private int _appIdUpdateTaskId = ZFTaskId.INVALID;
    private String _adId = null;
    private WeakReference<ZFUIRootWindow> _window = null;

    private final UnifiedBannerADListener _implListener = new UnifiedBannerADListener() {
        @Override
        public void onNoAD(AdError error) {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnError(zfjniPointerOwnerZFAd, error != null ? error.getErrorMsg() : "[GDT] onNoAD");
            }
        }

        @Override
        public void onADReceive() {
        }

        @Override
        public void onADExposure() {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnDisplay(zfjniPointerOwnerZFAd);
            }
        }

        @Override
        public void onADClosed() {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnClose(zfjniPointerOwnerZFAd);
            }
        }

        @Override
        public void onADClicked() {
            if (zfjniPointerOwnerZFAd != -1) {
                native_notifyAdOnClick(zfjniPointerOwnerZFAd);
            }
        }

        @Override
        public void onADLeftApplication() {
        }
    };

    private void _update() {
        if (ZFString.isEmpty(_adId)
                || _window == null || _window.get() == null
        ) {
            return;
        }

        if (impl == null) {
            impl = new UnifiedBannerView(
                    _window.get()
                    , _adId
                    , _implListener
            );
            this.addView(impl, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        impl.loadAD();
    }

}


package com.ZFFramework.ZFAd_impl;

import com.ZFFramework.ZF_impl.ZFMainEntry;
import com.bytedance.sdk.openadsdk.TTAdSdk;

public class ZFAdForBanner {

    public static void test() {
        TTAdSdk.getAdManager().createAdNative(ZFMainEntry.appContext());
    }

}

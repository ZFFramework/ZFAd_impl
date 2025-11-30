#include "ZFImpl_sys_Android_ZFAd_impl.h"
#include "ZFAd/protocol/ZFProtocolZFAdForBanner.h"

#define _ZFP_ZFImpl_sys_Android_ZFAdForBanner_DEBUG 1

#if ZF_ENV_sys_Android
ZF_NAMESPACE_GLOBAL_BEGIN

#define ZFImpl_sys_Android_JNI_ID_ZFAdForBanner ZFImpl_sys_Android_JNI_ID(ZFAd_1impl_ZFAdForBanner)
#define ZFImpl_sys_Android_JNI_NAME_ZFAdForBanner ZFImpl_sys_Android_JNI_NAME(ZFAd_impl.ZFAdForBanner)
ZFImpl_sys_Android_jclass_DEFINE(ZFImpl_sys_Android_jclassZFAdForBanner, ZFImpl_sys_Android_JNI_NAME_ZFAdForBanner)

ZFPROTOCOL_IMPLEMENTATION_BEGIN(ZFAdForBanner_sys_Android, ZFAdForBanner, v_ZFProtocolLevel::e_SystemNormal)
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_HINT("Android:View")
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_DEPENDENCY_BEGIN()
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_DEPENDENCY_ITEM(ZFUIView, "Android:View")
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_DEPENDENCY_END()

public:
    zfoverride
    virtual void *nativeAdCreate(ZF_IN ZFAdForBanner *ad) {
        JNIEnv *jniEnv = JNIGetJNIEnv();
        static jmethodID jmId = JNIUtilGetStaticMethodID(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), "native_nativeAdCreate",
            JNIGetMethodSig(JNIType::S_object_Object(), JNIParamTypeContainer()
                .add(JNIPointerJNIType)
            ).c_str());
        jobject tmp = JNIUtilCallStaticObjectMethod(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), jmId
                , JNIConvertZFObjectToJNIType(jniEnv, ad)
                );
        jobject ret = JNIUtilNewGlobalRef(jniEnv, tmp);
        JNIUtilDeleteLocalRef(jniEnv, tmp);
        _windowUpdateAttach(ad);
        return ret;
    }
    zfoverride
    virtual void nativeAdDestroy(ZF_IN ZFAdForBanner *ad) {
        _windowUpdateDetach(ad);

        JNIEnv *jniEnv = JNIGetJNIEnv();
        static jmethodID jmId = JNIUtilGetStaticMethodID(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), "native_nativeAdDestroy",
            JNIGetMethodSig(JNIType::S_void(), JNIParamTypeContainer()
                .add(JNIType::S_object_Object())
            ).c_str());
        JNIUtilCallStaticVoidMethod(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), jmId
                , (jobject)ad->nativeAd()
                );
        JNIUtilDeleteGlobalRef(jniEnv, (jobject)ad->nativeAd());
    }

    zfoverride
    virtual ZFUISize nativeAdMeasure(
            ZF_IN ZFAdForBanner *ad
            , ZF_IN const ZFUISize &sizeHint
            ) {
        ZFUIRect rect = ZFUIScaleTypeApply(
                v_ZFUIScaleType::e_FillCenter
                , ZFUIRectCreate(ZFUIPointZero(), ZFUISizeCreate(
                        sizeHint.width >= 0 ? sizeHint.width : _adWidth()
                        , sizeHint.height >= 0 ? sizeHint.height : _adHeight()
                        ))
                , ZFUISizeCreate(_adWidth(), _adHeight())
                );
        return ZFUISizeCreate(rect.width, rect.height);
    }

    zfoverride
    virtual void nativeAdUpdate(ZF_IN ZFAdForBanner *ad) {
        JNIEnv *jniEnv = JNIGetJNIEnv();
        static jmethodID jmId = JNIUtilGetStaticMethodID(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), "native_nativeAdUpdate",
            JNIGetMethodSig(JNIType::S_void(), JNIParamTypeContainer()
                .add(JNIType::S_object_Object())
                .add(JNIType::S_object_String())
                .add(JNIType::S_object_String())
            ).c_str());
        JNIUtilCallStaticVoidMethod(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), jmId
                , (jobject)ad->nativeAd()
                , (jobject)ZFImpl_sys_Android_zfstringToString(ad->appId())
                , (jobject)ZFImpl_sys_Android_zfstringToString(ad->adId())
                );
    }

private:
    static inline zffloat _adWidth(void) {
        return 6.4f;
    }
    static inline zffloat _adHeight(void) {
        return 1;
    }

    void _windowUpdateAttach(ZF_IN ZFAdForBanner *ad) {
        zfobj<ZFObject> eventHolder;
        ad->objectTag("_ZFP_ZFAdForBanner_sys_Android_eventHolder", eventHolder);
        ZFLISTENER_0(viewTreeInWindowOnUpdate
                ) {
            ZFAdForBanner *ad = zfargs.sender();
            if(ad->viewTreeInWindow()) {
                ZFUIRootWindow *window = ZFUIWindow::rootWindowForView(ad);
                if(window) {
                    JNIEnv *jniEnv = JNIGetJNIEnv();
                    static jmethodID jmId = JNIUtilGetStaticMethodID(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), "native_nativeAdWindowUpdate",
                        JNIGetMethodSig(JNIType::S_void(), JNIParamTypeContainer()
                            .add(JNIType::S_object_Object())
                            .add(JNIType::S_object_Object())
                        ).c_str());
                    JNIUtilCallStaticVoidMethod(jniEnv, ZFImpl_sys_Android_jclassZFAdForBanner(), jmId
                            , (jobject)ad->nativeAd()
                            , (jobject)window->nativeWindow()
                            );
                }
            }
        } ZFLISTENER_END()
        ZFObserverGroup(eventHolder, ad)
            .observerAdd(ZFUIView::E_ViewTreeInWindowOnUpdate(), viewTreeInWindowOnUpdate, ZFLevelZFFrameworkPostNormal)
            ;
    }
    void _windowUpdateDetach(ZF_IN ZFAdForBanner *ad) {
        zfauto eventHolder = ad->objectTagRemoveAndGet("_ZFP_ZFAdForBanner_sys_Android_eventHolder");
        if(eventHolder) {
            ZFObserverGroupRemove(eventHolder);
        }
    }
ZFPROTOCOL_IMPLEMENTATION_END(ZFAdForBanner_sys_Android)

ZF_NAMESPACE_GLOBAL_END

// ============================================================
JNI_METHOD_DECLARE_BEGIN(ZFImpl_sys_Android_JNI_ID_ZFAdForBanner
        , void, native_1notifyAdOnError
        , JNIPointer zfjniPointerOwnerZFAd
        , jstring errorHint
        ) {
#if _ZFP_ZFImpl_sys_Android_ZFAdForBanner_DEBUG
    ZFLogTrim("%s onError: %s", JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd), ZFImpl_sys_Android_zfstringFromString(errorHint));
#endif
    ZFPROTOCOL_ACCESS(ZFAdForBanner)->notifyAdOnError(
            JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd)
            , ZFImpl_sys_Android_zfstringFromString(errorHint)
            );
}
JNI_METHOD_DECLARE_END()

JNI_METHOD_DECLARE_BEGIN(ZFImpl_sys_Android_JNI_ID_ZFAdForBanner
        , void, native_1notifyAdOnDisplay
        , JNIPointer zfjniPointerOwnerZFAd
        ) {
#if _ZFP_ZFImpl_sys_Android_ZFAdForBanner_DEBUG
    ZFLogTrim("%s onDisplay", JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd));
#endif
    ZFPROTOCOL_ACCESS(ZFAdForBanner)->notifyAdOnDisplay(
            JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd)
            );
}
JNI_METHOD_DECLARE_END()

JNI_METHOD_DECLARE_BEGIN(ZFImpl_sys_Android_JNI_ID_ZFAdForBanner
        , void, native_1notifyAdOnClick
        , JNIPointer zfjniPointerOwnerZFAd
        ) {
#if _ZFP_ZFImpl_sys_Android_ZFAdForBanner_DEBUG
    ZFLogTrim("%s onClick", JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd));
#endif
    ZFPROTOCOL_ACCESS(ZFAdForBanner)->notifyAdOnClick(
            JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd)
            );
}
JNI_METHOD_DECLARE_END()

JNI_METHOD_DECLARE_BEGIN(ZFImpl_sys_Android_JNI_ID_ZFAdForBanner
        , void, native_1notifyAdOnClose
        , JNIPointer zfjniPointerOwnerZFAd
        ) {
#if _ZFP_ZFImpl_sys_Android_ZFAdForBanner_DEBUG
    ZFLogTrim("%s onClose: %s", JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd));
#endif
    ZFPROTOCOL_ACCESS(ZFAdForBanner)->notifyAdOnClose(
            JNIConvertZFObjectFromJNIType(jniEnv, zfjniPointerOwnerZFAd)
            );
}
JNI_METHOD_DECLARE_END()

#endif // #if ZF_ENV_sys_Android


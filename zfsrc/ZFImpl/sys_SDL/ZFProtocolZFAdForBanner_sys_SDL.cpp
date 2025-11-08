#include "ZFImpl_sys_SDL_ZFAd_impl.h"
#include "ZFAd/protocol/ZFProtocolZFAdForBanner.h"
#include "ZFUIKit/protocol/ZFProtocolZFUIView.h"

#if ZF_ENV_sys_SDL

ZF_NAMESPACE_GLOBAL_BEGIN

ZFPROTOCOL_IMPLEMENTATION_BEGIN(ZFAdForBannerImpl_sys_SDL, ZFAdForBanner, v_ZFProtocolLevel::e_SystemNormal)
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_HINT("ZFImpl_sys_SDL_View")
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_DEPENDENCY_BEGIN()
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_DEPENDENCY_ITEM(ZFUIView, "ZFImpl_sys_SDL_View")
    ZFPROTOCOL_IMPLEMENTATION_PLATFORM_DEPENDENCY_END()

public:
    zfoverride
    virtual void *nativeAdViewCreate(ZF_IN ZFAdForBanner *ad) {
        return zfnull;
    }
    zfoverride
    virtual void nativeAdViewDestroy(ZF_IN ZFAdForBanner *ad, ZF_IN void *nativeAd) {
    }
ZFPROTOCOL_IMPLEMENTATION_END(ZFAdForBannerImpl_sys_SDL)

ZF_NAMESPACE_GLOBAL_END

#endif // #if ZF_ENV_sys_SDL


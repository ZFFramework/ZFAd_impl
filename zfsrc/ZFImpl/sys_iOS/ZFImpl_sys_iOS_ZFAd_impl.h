#ifndef _ZFI_ZFImpl_sys_iOS_ZFAd_impl_h_
#define _ZFI_ZFImpl_sys_iOS_ZFAd_impl_h_

#include "../ZFImpl_ZFAd_impl.h"

#include "ZFImpl/sys_iOS/ZFImpl_sys_iOS_ZF_impl.h"

#if ZF_ENV_sys_iOS

#include <UIKit/UIKit.h>
#include "ZFCore.h"
#include "ZFImpl/sys_iOS/ZFMainEntry_sys_iOS.h"

ZF_NAMESPACE_GLOBAL_BEGIN

/**
 * @brief update app id, assert fail if alreay set a different one
 */
extern ZFLIB_ZFAd_impl zfautoT<ZFTaskId> ZFImpl_sys_iOS_ZFAd_appId(
        ZF_IN const zfstring &appId
        , ZF_IN_OPT const ZFListener &callback = zfnull
        );

ZF_NAMESPACE_GLOBAL_END
#endif // #if ZF_ENV_sys_iOS
#endif // #ifndef _ZFI_ZFImpl_sys_iOS_ZFAd_impl_h_


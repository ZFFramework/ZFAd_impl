#include "ZFImpl_sys_iOS_ZFAd_impl.h"

#if ZF_ENV_sys_iOS

#import "GDTSDKConfig.h"

ZF_NAMESPACE_GLOBAL_BEGIN

zfautoT<ZFTaskId> ZFImpl_sys_iOS_ZFAd_appId(
        ZF_IN const zfstring &appId
        , ZF_IN_OPT const ZFListener &callback /* = zfnull */
        ) {
    static zfbool _needInit = zftrue;
    static zfbool _initRunning = zffalse;
    static zfstring _appId;
    static ZFCoreArray<ZFListener> _callbackList;
    if((!_needInit || _initRunning) && _appId != appId) {
        ZFCoreCriticalMessageTrim("[ZFAd] appId already registered: %s => %s", _appId, appId);
        callback.execute();
        return zfnull;
    }
    if(!_needInit) {
        callback.execute();
        return zfnull;
    }
    if(!_initRunning) {
        if(!appId) {
            callback.execute();
            return zfnull;
        }
        _initRunning = zftrue;
        _appId = appId;
        if(![GDTSDKConfig initWithAppId:[NSString stringWithUTF8String:appId.cString()]]) {
            ZFLogTrim("[ZFAd] %s init fail", appId);
            callback.execute();
            return zfnull;
        }
        [GDTSDKConfig startWithCompletionHandler:^(BOOL success, NSError *error) {
            dispatch_async(dispatch_get_main_queue(), ^{
                if(!success) {
                    ZFLogTrim("[ZFAd] %s setup fail", appId);
                }
                _initRunning = zffalse;
                ZFCoreArray<ZFListener> callbackList;
                _callbackList.swap(callbackList);
                for(zfindex i = 0; i < callbackList.count(); ++i) {
                    callbackList[i].execute();
                }
            });
        }];
    }
    if(!callback) {
        return zfnull;
    }
    _callbackList.add(callback);
    ZFLISTENER_1(onStop
            , ZFListener, callback
            ) {
        _callbackList.remove(callback);
    } ZFLISTENER_END()
    return zfobj<ZFTaskIdBasic>(onStop);
}

ZF_NAMESPACE_GLOBAL_END
#endif // #if ZF_ENV_sys_iOS


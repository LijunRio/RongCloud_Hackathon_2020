package cn.rongcloud.scriptgame.model;

import java.util.List;

import cn.rongcloud.scriptgame.bean.repo.NetResult;
import cn.rongcloud.scriptgame.bean.repo.RefreshTokenRepo;
import cn.rongcloud.scriptgame.bean.repo.RoomMemberRepo;
import cn.rongcloud.scriptgame.bean.repo.UserLoginRepo;
import cn.rongcloud.scriptgame.bean.repo.VisitorLoginRepo;
import cn.rongcloud.scriptgame.bean.req.SendCodeReq;
import cn.rongcloud.scriptgame.bean.req.UserInfoReq;
import cn.rongcloud.scriptgame.bean.req.UserLoginReq;
import cn.rongcloud.scriptgame.bean.req.VisitorLoginReq;
import cn.rongcloud.scriptgame.common.NetStateLiveData;
import cn.rongcloud.scriptgame.manager.CacheManager;
import cn.rongcloud.scriptgame.net.client.RetrofitClient;
import cn.rongcloud.scriptgame.net.service.UserService;
import cn.rongcloud.scriptgame.util.DeviceUtil;
import cn.rongcloud.scriptgame.util.RandomUtil;

/**
 * 用户模块数据层(M层)
 */
public class UserModel {

    private UserService userService;

    public UserModel(RetrofitClient client) {
        userService = client.createService(UserService.class);
    }

    public NetStateLiveData<NetResult<Void>> sendCode(String mobile) {
        return userService.sendCode(new SendCodeReq(mobile));
    }

    public NetStateLiveData<RefreshTokenRepo> refreshToken() {
        return userService.refreshToken();
    }

    public NetStateLiveData<VisitorLoginRepo> visitorLogin() {
        String userName = RandomUtil.getUserName();
        String portrait = RandomUtil.getUserHeadImage();
        String deviceId = DeviceUtil.getUniqueDeviceId();
        CacheManager.getInstance().cacheDeviceId(deviceId);
        VisitorLoginReq visitorLoginReq = new VisitorLoginReq(userName, portrait, deviceId);
        return userService.visitorLogin(visitorLoginReq);
    }

    public NetStateLiveData<UserLoginRepo> login(String mobile, String authCode) {
        String userName = RandomUtil.getUserName();
        String portrait = RandomUtil.getUserHeadImage();
        String deviceId = CacheManager.getInstance().getDeviceId();
        return userService.userLogin(new UserLoginReq(mobile, authCode, userName, portrait, deviceId));
    }

    public NetStateLiveData<NetResult<List<RoomMemberRepo.MemberBean>>> userbatch(List<String> userIds) {
        return userService.userBatch(new UserInfoReq(userIds));
    }

}

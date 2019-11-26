package com.talentfootpoint.talentfootpoint.facade;


import com.talentfootpoint.talentfootpoint.entity.WxUser;
import com.talentfootpoint.talentfootpoint.entity.WxUserVo;

import java.util.List;

/**
 * @author lsb
 */
public interface WxUserFacade {
    /**
     * 存储用户数据
     * @param wxUser 对象
     * @return 是否成功
     */
    int saveUserData(WxUser wxUser);
    /**
     * 获取用户数据
     * @param wxUserVo
     * @return 用户数据
     */
    List<WxUserVo> getAllWxUser(WxUserVo wxUserVo);

    /**
     * 修改数据
     * @param wxUserVo
     * @return 是否更改成功
     */
    int updateWxUser(WxUserVo wxUserVo);

}

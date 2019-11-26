package com.talentfootpoint.talentfootpoint.service.impl;


import com.talentfootpoint.talentfootpoint.entity.WxUser;
import com.talentfootpoint.talentfootpoint.entity.WxUserVo;
import com.talentfootpoint.talentfootpoint.service.WxUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lsb
 */
@Service
public class WxUserServiceImpl implements WxUserService {
    @Override
    public int saveUserData(WxUser wxUser) {
        return 0;
    }

    @Override
    public List<WxUserVo> getAllWxUser(WxUserVo wxUserVo) {
        return null;
    }

    @Override
    public int updateWxUser(WxUserVo wxUserVo) {
        return 0;
    }
}

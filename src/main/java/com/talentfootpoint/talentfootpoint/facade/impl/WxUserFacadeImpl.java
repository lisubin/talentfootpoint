package com.talentfootpoint.talentfootpoint.facade.impl;


import com.talentfootpoint.talentfootpoint.entity.WxUser;
import com.talentfootpoint.talentfootpoint.entity.WxUserVo;
import com.talentfootpoint.talentfootpoint.facade.WxUserFacade;
import com.talentfootpoint.talentfootpoint.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lsb
 */
@Component("wxUserFacade")
public class WxUserFacadeImpl implements WxUserFacade {
    @Autowired
    private WxUserService wxUserService;
    @Override
    public int saveUserData(WxUser wxUser) {
        return wxUserService.saveUserData(wxUser);
    }

    @Override
    public List<WxUserVo> getAllWxUser(WxUserVo wxUserVo) {
        return wxUserService.getAllWxUser(wxUserVo);
    }

    @Override
    public int updateWxUser(WxUserVo wxUserVo) {
        return wxUserService.updateWxUser(wxUserVo);
    }
}

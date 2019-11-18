package com.talentfootpoint.talentfootpoint.mapper;

import com.onething.oneyard.entity.WxUser;
import com.onething.oneyard.entity.WxUserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lsb
 */
@Mapper
public interface WxUserMapper {
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

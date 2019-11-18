package com.talentfootpoint.talentfootpoint.controller;

import com.alibaba.fastjson.JSONObject;
import com.onething.oneyard.entity.WxUserVo;
import com.onething.oneyard.facade.WxUserFacade;
import com.onething.oneyard.util.JsonResult;
import com.onething.oneyard.util.WeiXinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author lsb
 */
@RestController
public class WxUserController {

    @Autowired
    private WxUserFacade wxUserFacade;

    /**
     * 用户登录
     * @param request
     * @param response
     * @param json
     * @return
     * @throws IOException
     */
    @CrossOrigin(origins = "*")
    @RequestMapping("userLogin")
    @ResponseBody
    public Object userLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) throws IOException {

        JSONObject jsonObject = JSONObject.parseObject(json);
        JsonResult jsonResult = new JsonResult();
        WeiXinUtils weiXinUtils = new WeiXinUtils();
        String code = jsonObject.getString("code");
        if (code == null) {
            jsonResult.setMsg("code为空");
        } else {
            JSONObject jsonObject1 = (JSONObject) weiXinUtils.getCode(request, code);
            String openId = jsonObject1.get("openId").toString();

            WxUserVo wxUserVo = new WxUserVo();
            wxUserVo.setOpenId(openId);
            List<WxUserVo> allWxUser = wxUserFacade.getAllWxUser(wxUserVo);
            if (allWxUser.size()>0) {
                //跳转到问卷调查页面
                response.sendRedirect("http://wj.qq.com/s2/3976301/9647/");
            } else {
                //跳到index页面让用户记录数据
                response.sendRedirect("http://110.14.204.72:8085/index.html");
            }
        }


        return jsonResult;
    }




}

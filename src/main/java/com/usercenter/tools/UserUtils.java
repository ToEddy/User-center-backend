package com.usercenter.tools;

import cn.hutool.core.bean.BeanUtil;
import com.usercenter.model.dto.UserDto;
import com.usercenter.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import static com.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author eddy
 */
public class UserUtils {
    /**
     * 鉴权
     *
     * @param request
     * @return
     */
    public static boolean authentication(HttpServletRequest request) {
        // 鉴权
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (attribute == null) {
            //没有登录信息
            return false;
        }
        UserDto userDto = (UserDto) attribute;
        Integer userRole = userDto.getUserRole();
        if (userRole == 1 || userRole == 2) {
            //鉴权成功，管理员
            return true;
        }
        return false;
    }


    /**
     * 用户信息脱敏
     *
     * @param user
     * @return
     */
    public static UserDto getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        UserDto safetyUser = BeanUtil.copyProperties(user, UserDto.class);
        return safetyUser;
    }

}

package com.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usercenter.model.dto.UserDto;
import com.usercenter.model.dto.UserDtoWithPasswd;
import com.usercenter.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author eddy
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-01-20 21:09:48
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userDtoWithPasswd
     * @return
     */
    long userRegister(UserDtoWithPasswd userDtoWithPasswd);

    /**
     * 用户登录
     *
     * @param userDtoWithPasswd
     * @param request
     * @return
     */
    UserDto userLogin(UserDtoWithPasswd userDtoWithPasswd, HttpServletRequest request);

    /**
     * 普通用户查询好友，或者加好友(他只能通过用户名查询)
     *
     * @param username
     * @return
     */
    List<UserDto> userSearchByName(String username);

    /**
     * 管理员通过账号进行查询
     *
     * @param userAccount
     * @return
     */
    UserDto userSearchByAccount(String userAccount);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    boolean userDelete(long id);

    /**
     * 普通用户自主注销账号
     *
     * @return
     */
    boolean userCancel(HttpServletRequest request);

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    UserDto getCurrentUser(HttpServletRequest request);

    /**
     * 升级为管理员(需要一个平台<我是最高权限>才能给用户升级)
     *
     * @param upgradeName 通过告知给管理员你的账号是什么
     * @param request
     * @return
     */
    boolean upToAdmin(String upgradeName, HttpServletRequest request);
}

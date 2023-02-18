package com.usercenter.controller;

import com.usercenter.model.domain.ResultData;
import com.usercenter.model.dto.UserDto;
import com.usercenter.model.dto.UserDtoWithPasswd;
import com.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.usercenter.tools.UserUtils.authentication;

/**
 * @author eddy
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResultData<Long> UserRegister(@RequestBody UserDtoWithPasswd userDtoWithPasswd) {
        long ResultID = 0;
        try {
            //当注册用户出现异常的时候，也就是包括我们在service层自定义的异常，都会被catch掉
            ResultID = userService.userRegister(userDtoWithPasswd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //没有异常就会返回一个结果
        return ResultData.success(ResultID, "用户注册成功");
    }

    @PostMapping("login")
    public ResultData<UserDto> UserLogin(@RequestBody UserDtoWithPasswd userDtoWithPasswd, HttpServletRequest request) {
        UserDto safetyUser = null;
        try {
            safetyUser = userService.userLogin(userDtoWithPasswd, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.success(safetyUser, "用户登陆成功");
    }

    @GetMapping("/current")
    public ResultData<UserDto> getCurrentUser(HttpServletRequest request) {
        UserDto currentUser = null;
        try {
            currentUser = userService.getCurrentUser(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.success(currentUser, "获取当前用户信息成功");
    }

    // todo 用户昵称是否唯一？
    @GetMapping("/searchByName")
    public ResultData<List<UserDto>> UserSearchByName(String username) {
        List<UserDto> userDtoList = null;

        userDtoList = userService.userSearchByName(username);

        if (userDtoList == null) {
            return ResultData.fail(100, "查找用户失败");
        }
        return ResultData.success(userDtoList, "查找用户成功");
    }

    @GetMapping("/searchByAccount")
    public ResultData<UserDto> UserSearchByAccount(String userAccount, HttpServletRequest request) {
        //鉴权
        boolean authentication = authentication(request);
        if (!authentication) {
            return new ResultData(10000, "没有权限");
        }
        UserDto userDto = null;
        try {
            userDto = userService.userSearchByAccount(userAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userDto == null) {
            return ResultData.fail(100, "查找用户失败");
        }
        return ResultData.success(userDto, "查找用户成功");
    }

    @PostMapping("/cancel")
    public ResultData<Boolean> UserCancel(HttpServletRequest request) {
        boolean isCancel = userService.userCancel(request);
        if (!isCancel) {
            return ResultData.fail(103, "用户注销失败");
        }

        return ResultData.success(isCancel, "用户注销成功");
    }

    @PostMapping("/delete")
    public ResultData<Boolean> UserDelete(Long id, HttpServletRequest request) {
        //鉴权
        boolean authentication = authentication(request);
        if (!authentication) {
            return new ResultData(10000, "没有权限");
        }
        boolean isDelete = userService.userDelete(id);
        if (!isDelete) {
            return ResultData.fail(102, "用户删除失败");
        }
        return ResultData.success(isDelete, "删除用户成功");
    }

    @PostMapping("/logout")
    public ResultData<Boolean> userLogout(HttpServletRequest request) {
        boolean isLogout = userService.userLogout(request);
        if (!isLogout) {
            return ResultData.fail(102, "用户退出登录失败");
        }
        return ResultData.success(isLogout, "用户成功退出登录");
    }

    @PostMapping("/upgrade")
    public ResultData<Boolean> userUpgrade(String upgradeName, HttpServletRequest request) {
        //鉴权
        boolean authentication = authentication(request);
        if (!authentication) {
            return new ResultData(10000, "没有权限");
        }
        boolean isUpgrade = userService.upToAdmin(upgradeName, request);
        if (!isUpgrade) {
            return ResultData.fail(102, "用户升级失败");
        }
        return ResultData.success(isUpgrade, "用户升级成功");
    }
}

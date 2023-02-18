package com.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usercenter.exception.BusinessException;
import com.usercenter.mapper.UserMapper;
import com.usercenter.model.dto.UserDto;
import com.usercenter.model.dto.UserDtoWithPasswd;
import com.usercenter.model.entity.User;
import com.usercenter.service.UserService;
import com.usercenter.tools.UserUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.usercenter.constant.UserConstant.SALT;
import static com.usercenter.constant.UserConstant.USER_LOGIN_STATE;
import static com.usercenter.exception.ErrorCode.NULL_ERR_CODE;
import static com.usercenter.exception.ErrorCode.PARAMS_ERR_CODE;
import static com.usercenter.tools.UserUtils.getSafetyUser;

/**
 * @author eddy
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-01-20 21:09:48
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(UserDtoWithPasswd userDtoWithPasswd) {
        if (userDtoWithPasswd == null) {
            throw new BusinessException(NULL_ERR_CODE);
        }
        String account = userDtoWithPasswd.getUserAccount();
        String password = userDtoWithPasswd.getUserPassword();
        String checkPassword = userDtoWithPasswd.getCheckPassword();
        //1.账号，密码和校验密码都不能为空
        if (StringUtils.isAnyBlank(account, password, checkPassword)) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //2.账号长度不能小于4位
        if (account.length() < 4) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //3.账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (matcher.find()) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //4.密码长度不能小于8位
        if (password.length() < 8) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //5. 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //6.账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", account);
        boolean exists = userMapper.exists(queryWrapper);
        if (exists) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //创建用户
        User user = new User();
        user.setUserAccount(account);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        return user.getId();
    }

    @Override
    public UserDto userLogin(UserDtoWithPasswd userDtoWithPasswd, HttpServletRequest request) {
        if (userDtoWithPasswd == null) {
            throw new BusinessException(NULL_ERR_CODE);
        }
        String account = userDtoWithPasswd.getUserAccount();
        String password = userDtoWithPasswd.getUserPassword();
        //1.账号，密码都不能为空
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //2.账号长度不能小于4位
        if (account.length() < 4) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //3.账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (matcher.find()) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //4.密码长度不能小于8位
        if (password.length() < 8) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //todo 5.检查账号和密码是否匹配 （那会不会是账号不存在呢？）
        User user;
        //5.1 先检查账号是不是存在的 = > 也就是用户输入的账号有没有错误？
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", account);
        boolean exists = userMapper.exists(queryWrapper);
        if (!exists) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        queryWrapper.eq("userPassword", encryptPassword);
        user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(PARAMS_ERR_CODE);
        }
        //用户脱敏
        UserDto safetyUser = getSafetyUser(user);
        //将用户的登录态存储到服务器
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public List<UserDto> userSearchByName(String username) {
        if (username == null) {
            throw new BusinessException(NULL_ERR_CODE);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        List<User> userList = userMapper.selectList(queryWrapper);
        if (userList == null || userList.size() == 0) {
            return null;
        }
        //将User类型的集合转换成UserDto类型的集合
        return userList.stream().map(UserUtils::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public UserDto userSearchByAccount(String userAccount) {
        if (userAccount == null) {
            throw new BusinessException(NULL_ERR_CODE);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        UserDto safetyUser = getSafetyUser(user);
        return safetyUser;
    }

    @Override
    public boolean userDelete(long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        int delete = userMapper.delete(queryWrapper);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean userCancel(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (attribute == null) {
            return false;
        }
        UserDto userDto = (UserDto) attribute;
        String userAccount = userDto.getUserAccount();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        int delete = userMapper.delete(queryWrapper);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        try {
            request.getSession().removeAttribute(USER_LOGIN_STATE);
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public UserDto getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            return null;
        }
        UserDto safetyUser = (UserDto) userObj;
        return safetyUser;
    }

    /**
     * @param upgradeName 通过告知给管理员你的账号是什么
     * @param request
     * @return
     */
    @Override
    public boolean upToAdmin(String upgradeName, HttpServletRequest request) {
        if (upgradeName == null) {
            throw new BusinessException(NULL_ERR_CODE);
        }
        //获取顶级管理员的权限
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        UserDto adminUser = (UserDto) userObj;
        Integer adminUserUserRole = adminUser.getUserRole();
        if (adminUser == null || adminUserUserRole == 1) {
            return false;
        }
        //权限足够
        if (adminUserUserRole == 2) {
            UserDto upgradeUserDto = userSearchByAccount(upgradeName);
            upgradeUserDto.setUserRole(1);
            int isUpdateWithRole = userMapper.updateUserRoleById(upgradeUserDto);
            if (isUpdateWithRole > 0) {
                return true;
            }
        }
        return false;
    }
}





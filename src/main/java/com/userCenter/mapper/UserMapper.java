package com.userCenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.userCenter.model.dto.UserDto;
import com.userCenter.model.entity.User;

/**
 * @author 47607
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2023-01-20 21:09:48
 * @Entity com.usercenter.domain.User
 */
public interface UserMapper extends BaseMapper<User> {
    int updateUserRoleById(UserDto userDto);

}





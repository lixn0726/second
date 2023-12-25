package com.notme.second.universal.services.user.mapper;

import com.notme.second.universal.services.user.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author listen
 **/
@Mapper
public interface UserMapper {

    User queryUserById(Integer userId);

}

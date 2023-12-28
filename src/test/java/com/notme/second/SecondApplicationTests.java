package com.notme.second;

import com.notme.second.universal.services.user.mapper.UserMapper;
import com.notme.second.universal.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SecondApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testExtensionQuery() {
        User user = userMapper.queryUserById(1);
        System.out.println(user);
    }

}

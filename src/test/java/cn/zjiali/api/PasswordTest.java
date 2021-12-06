package cn.zjiali.api;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zJiaLi
 * @since 2021-12-04 14:21
 */
public class PasswordTest {

    @Test
    public void test(){
        String encode = new BCryptPasswordEncoder().encode("123456789");
        System.out.println(encode);
    }
}

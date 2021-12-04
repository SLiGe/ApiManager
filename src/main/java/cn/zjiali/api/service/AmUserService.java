package cn.zjiali.api.service;

import cn.zjiali.api.mapper.AmUserMapper;
import cn.zjiali.api.model.entity.AmRole;
import cn.zjiali.api.model.entity.AmUser;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zJiaLi
 * @since 2021-12-04 09:36
 */
@Service
public class AmUserService extends ServiceImpl<AmUserMapper, AmUser> implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AmUser user = getOne(Wrappers.<AmUser>lambdaQuery().eq(AmUser::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        List<AmRole> roles = this.baseMapper.selectUserRoles(user.getUserId());
        user.setRoles(roles);
        return user;
    }
}

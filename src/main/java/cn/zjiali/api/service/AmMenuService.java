package cn.zjiali.api.service;

import cn.zjiali.api.mapper.AmMenuMapper;
import cn.zjiali.api.model.entity.AmMenu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zJiaLi
 * @since 2021-12-04 10:09
 */
@Service
public class AmMenuService extends ServiceImpl<AmMenuMapper, AmMenu> {

    public List<AmMenu> selectAllMenuWithRole(){
        return this.baseMapper.selectAllMenuWithRole();
    }

}

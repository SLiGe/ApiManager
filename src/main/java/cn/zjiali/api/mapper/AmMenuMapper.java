package cn.zjiali.api.mapper;

import cn.zjiali.api.model.entity.AmMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ${DESC}
 *
 * @author zJiaLi
 * @since 2021-12-03 22:33
 */
@Mapper
public interface AmMenuMapper extends BaseMapper<AmMenu> {
    List<AmMenu> selectAllMenuWithRole();
}
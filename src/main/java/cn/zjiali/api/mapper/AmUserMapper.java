package cn.zjiali.api.mapper;

import cn.zjiali.api.model.entity.AmRole;
import cn.zjiali.api.model.entity.AmUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${DESC}
 *
 * @author zJiaLi
 * @since 2021-12-03 22:33
 */
@Mapper
public interface AmUserMapper extends BaseMapper<AmUser> {
    /**
     * 根据用户ID查询权限表
     *
     * @param userId 用户ID
     * @return 权限
     */
    List<AmRole> selectUserRoles(@Param("userId") Long userId);
}
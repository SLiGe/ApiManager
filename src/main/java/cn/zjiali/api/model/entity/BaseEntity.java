package cn.zjiali.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.util.Date;

/**
 * @author zJiaLi
 * @since 2021-12-03 22:32
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}

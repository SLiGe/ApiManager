package cn.zjiali.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限表
 *
 * @author zJiaLi
 * @since 2021-12-03 22:33
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "am_role")
public class AmRole extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 权限代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 权限名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 显示顺序
     */
    @TableField(value = "role_sort")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @TableField(value = "`status`")
    private String status;


    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_CODE = "code";

    public static final String COL_NAME = "name";

    public static final String COL_ROLE_SORT = "role_sort";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_REMARK = "remark";
}
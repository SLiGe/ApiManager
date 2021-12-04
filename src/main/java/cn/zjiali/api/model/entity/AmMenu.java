package cn.zjiali.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;

/**
 * 菜单权限表
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
@TableName(value = "am_menu")
public class AmMenu extends BaseEntity {
    /**
     * 菜单ID
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 请求地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 打开方式（menuItem页签 menuBlank新窗口）
     */
    @TableField(value = "target")
    private String target;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @TableField(value = "menu_type")
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @TableField(value = "visible")
    private String visible;

    /**
     * 是否刷新（0刷新 1不刷新）
     */
    @TableField(value = "is_refresh")
    private String isRefresh;

    /**
     * 权限标识
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

    @TableField(exist = false)
    private List<AmRole> roles;

    public static final String COL_MENU_ID = "menu_id";

    public static final String COL_MENU_NAME = "menu_name";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_ORDER_NUM = "order_num";

    public static final String COL_URL = "url";

    public static final String COL_TARGET = "target";

    public static final String COL_MENU_TYPE = "menu_type";

    public static final String COL_VISIBLE = "visible";

    public static final String COL_IS_REFRESH = "is_refresh";

    public static final String COL_PERMS = "perms";

    public static final String COL_ICON = "icon";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_UPDATE_TIME = "update_time";
}
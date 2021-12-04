package cn.zjiali.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户表
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
@TableName(value = "am_user")
public class AmUser extends BaseEntity implements UserDetails {
    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 登录名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 用户类型用户类型（00系统用户 01注册用户）
     */
    @TableField(value = "user_type")
    private String userType;

    /**
     * 用户姓名
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 邮箱地址
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 是否启用(0未启用1启用)
     */
    @TableField(value = "`enable`")
    private String enable;

    /**
     * 最后登录IP
     */
    @TableField(value = "login_ip")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @TableField(value = "login_date")
    private Date loginDate;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private List<AmRole> roles;


    public static final String COL_USER_ID = "user_id";

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWORD = "password";

    public static final String COL_USER_TYPE = "user_type";

    public static final String COL_NAME = "name";

    public static final String COL_PHONE = "phone";

    public static final String COL_SEX = "sex";

    public static final String COL_EMAIL = "email";

    public static final String COL_AVATAR = "avatar";

    public static final String COL_ENABLE = "enable";

    public static final String COL_LOGIN_IP = "login_ip";

    public static final String COL_LOGIN_DATE = "login_date";

    public static final String COL_REMARK = "remark";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_UPDATE_TIME = "update_time";

    @Override
    public boolean equals(Object o) {
        if (o instanceof AmUser) {
            return this.username.equals(((AmUser) o).username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>(this.roles.size());
        for (AmRole role : this.roles) {
            simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(role.getCode()));
        }
        return simpleGrantedAuthorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "1".equals(this.enable);
    }
}
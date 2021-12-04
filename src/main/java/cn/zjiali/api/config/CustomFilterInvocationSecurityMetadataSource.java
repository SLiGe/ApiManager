package cn.zjiali.api.config;

import cn.zjiali.api.model.entity.AmMenu;
import cn.zjiali.api.model.entity.AmRole;
import cn.zjiali.api.service.AmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2021-12-04 10:05
 */
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private AmMenuService amMenuService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        if ("/login".equals(requestUrl) || requestUrl.contains("logout")) {
            return null;
        }
        List<AmMenu> amMenus = amMenuService.selectAllMenuWithRole();
        for (AmMenu menu : amMenus) {
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                List<AmRole> roles = menu.getRoles();
                String[] str = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getCode();
                }
                return SecurityConfig.createList(str);
            }
        }
        return SecurityConfig.createList("ROLE_ANONYMOUS");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Autowired
    public void setAmMenuService(AmMenuService amMenuService) {
        this.amMenuService = amMenuService;
    }
}

package kr.swdl.sprint2.auth;

import kr.swdl.sprint2.service.properties.AccountRole;
import kr.swdl.sprint2.vo.User;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserLoginDetail implements UserDetails {
    private AccountRole accountRole;
    private User user;

    public UserLoginDetail(User user, AccountRole accountRolerole) {
        this.user = user;
        this.accountRole = accountRolerole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public @Nullable String getAuthority() {
                String role = "";

                if(user.getAccountRole() == accountRole.admin().code())
                    role = accountRole.admin().authority();
                else if(user.getAccountRole() == accountRole.user().code())
                    role = accountRole.user().authority();

                return role;
            }
        });
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }
}

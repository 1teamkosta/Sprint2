package kr.swdl.sprint2.auth;

import kr.swdl.sprint2.repository.LoginRepository;
import kr.swdl.sprint2.service.properties.AccountRole;
import kr.swdl.sprint2.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {
    private final LoginRepository repository;
    private final AccountRole accountRole;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.getUserById(username);
        if(user == null)
            return null;

        return new UserLoginDetail(user,  accountRole);
    }
}

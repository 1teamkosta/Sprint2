package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.service.properties.AccountRole;
import kr.swdl.sprint2.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LoginRepositoryTest {
    @Autowired
    private LoginRepository repository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private AccountRole role;

    @Test
    public void getUserById() {
        Assertions.assertNotNull(repository.getUserById("User20"));
        Assertions.assertNull(repository.getUserById("dvasdvab"));
    }

    @Test
    public void addUser() {
        repository.addUser(User.builder().id("admin01").password(encoder.encode("1234"))
                .nickname("임시관리자").email("admin@a.com").accountRole(role.admin().code()).build());

        Assertions.assertNotNull(repository.getUserById("admin01"));
    }
}

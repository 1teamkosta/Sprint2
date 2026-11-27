package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.UserJoinDTO;
import kr.swdl.sprint2.repository.LoginRepository;
import kr.swdl.sprint2.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository repository;
    private final BCryptPasswordEncoder encoder;

    public void addUser(UserJoinDTO dto) {
        repository.addUser(User.builder().accountRole(dto.getAccountRole()).id(dto.getId())
                .password(encoder.encode(dto.getPassword())).email(dto.getEmail()).nickname(dto.getNickname()).build());
    }
}

package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    /**
     * loginId 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    @Override
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    /**
     * nickname 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    @Override
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 회원가입 기능 1
     * 화면에서 JoinRequest(loginId, password, nickname)을 입력받아 User로 변환 후 저장
     * loginId, nickname 중복 체크는 Controller에서 진행 => 에러 메세지 출력을 위해
     */
//    @Override
//    public void join(UserDto req) {
//
//    }

    /**
     * 회원가입 기능 2
     * 화면에서 JoinRequest(loginId, password, nickname)을 입력받아 User로 변환 후 저장
     * 회원가입 1과는 달리 비밀번호를 암호화해서 저장
     * loginId, nickname 중복 체크는 Controller에서 진행 => 에러 메세지 출력을 위해
     */
    @Override
    public void createUser(UserDto req) {
        req.setPassword(encoder.encode(req.getPassword()));
        UserEntity user = new UserEntity().createUserEntity(req);
        userRepository.save(user);
    }

    /**
     *  로그인 기능
     *  화면에서 LoginRequest(loginId, password)을 입력받아 loginId와 password가 일치하면 User return
     *  loginId가 존재하지 않거나 password가 일치하지 않으면 null return
     */
    @Override
    public UserDto login(UserDto req) {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(req.getLoginId());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        UserEntity userEntity = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if(!userEntity.getPassword().equals(req.getPassword())) {
            return null;
        }

        UserDto userDto = mapper.map(userEntity, UserDto.class);

        return userDto;
    }

    /**
     * userId(Long)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * userId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * userId로 찾아온 User가 존재하면 User return
     */
    @Override
    public UserDto getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        UserEntity userEntity = optionalUser.get();
        UserDto userDto = mapper.map(userEntity, UserDto.class);

        return userDto;
    }

    /**
     * loginId(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * loginId로 찾아온 User가 존재하면 User return
     */
    @Override
    public UserDto getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        UserEntity userEntity = optionalUser.get();
        UserDto userDto = mapper.map(userEntity, UserDto.class);

        return userDto;
    }
}

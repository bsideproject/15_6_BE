package bside.NotToDoClub.domain_name.auth.config.auth;

import bside.NotToDoClub.domain_name.auth.config.auth.PrincipalDetails;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 UserDetailService 구현체
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserEntity user = userRepository.findByLoginId(username)
                    .orElseThrow(() -> {
                        return new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
                    });
            return new PrincipalDetails(user);
    }
}

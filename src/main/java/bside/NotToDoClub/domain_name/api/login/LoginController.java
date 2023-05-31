package bside.NotToDoClub.domain_name.api.login;

import bside.NotToDoClub.domain_name.api.login.vo.JoinRequest;
import bside.NotToDoClub.domain_name.api.login.vo.LoginRequest;
import bside.NotToDoClub.domain_name.api.login.vo.ResponseUser;
import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/security-login")
public class LoginController {

    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping(value = {"", "/"})
    public String home(Model model, Authentication auth) {

        if(auth != null) {
            UserDto loginUser = userService.getLoginUserByLoginId(auth.getName());
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }

        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult) {

        // loginId 중복 체크
        if(userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }
        // 닉네임 중복 체크
        if(userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }
        // password와 passwordCheck가 같은지 체크
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "바밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()) {
            return "join";
        }

        UserDto userDto = mapper.map(joinRequest, UserDto.class);
        userService.createUser(userDto);
        return "redirect:/security-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/info")
    //@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String userInfo(Model model, Authentication auth) {

//        User loginUser = userService.getLoginUserByLoginId(auth.getName());
        UserDto userDto = userService.getLoginUserByLoginId(auth.getName());
        ResponseUser loginUser = mapper.map(userDto, ResponseUser.class);
        model.addAttribute("user", loginUser);

        return "info";
    }

    @GetMapping("/admin")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/authentication-fail")
    public String authenticationFail() {
        return "errorPage/authenticationFail";
    }

    @GetMapping("/authorization-fail")
    public String authorizationFail() {
        return "errorPage/authorizationFail";
    }
}

package RankingCoin.Coin.webController;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.service.CoinService;
import RankingCoin.Coin.service.UserService;
import RankingCoin.Coin.webController.DTO.UserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CoinService coinService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/user/new")
    public String createForm(Model model){
        model.addAttribute("userForm", new UserForm());
        return "user/createUserForm";
    }

    @PostMapping(value = "/user/new")
    public String create(@Valid UserForm userForm, BindingResult result){
        if(result.hasErrors()){
            return "user/createUserForm";
        }

        if(!userForm.getPwd().equals(userForm.getPwdCh())){
            result.rejectValue("pwdCh","3","비밀번호가 일치하지 않습니다.");
            return "user/createUserForm";
        }

        User user = User.createUser(userForm.getId(), passwordEncoder.encode(userForm.getPwd()));
        if(!userService.join(user)){
            result.rejectValue("id","2","이미 사용중인 ID입니다.");
            return "user/createUserForm";
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/user/favor/{id}")
    public String setFavor(@PathVariable("id")Long id, @RequestParam(value = "name") String username){
        Coin coin = coinService.find(id);
        userService.addFavor(username, coin.getMarket());

        return "redirect:/coin/"+ id+ "/view";
    }

}

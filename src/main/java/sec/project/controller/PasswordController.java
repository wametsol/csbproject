package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import sec.project.domain.Signup;
import sec.project.repository.UserRepository;

@Controller
public class PasswordController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder encoder;
    
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String loadPassword() {
        return "password";
    }
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String submitForm(Authentication auth, @RequestParam String password) {
        String username = userDetailsService.loadUserByUsername(auth.getName()).getUsername();
        userDetailsService.changePassword(username, encoder.encode(password));
        return "redirect:/index";
    }
    

    @RequestMapping(value = "/password/{newpassword}", method = RequestMethod.GET)
    public String submitPassword(Authentication auth, @PathVariable String newpassword) {
        String username = userDetailsService.loadUserByUsername(auth.getName()).getUsername();
        userDetailsService.changePassword(username, encoder.encode(newpassword));
        return "redirect:/index";
    }

}
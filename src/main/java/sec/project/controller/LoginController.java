package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import sec.project.domain.Signup;
import sec.project.domain.User;
import sec.project.repository.MessageRepository;
import sec.project.repository.SignupRepository;
import sec.project.repository.UserRepository;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private MessageRepository msg;
    
    @RequestMapping({"*", "/"})
    public String defaultMapping() {
        return "redirect:/index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loadLogin() {
        return "login";
    }
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String loadIndex(Model model, Authentication auth) {        
        User user = userDetailsService.getUser(userDetailsService.loadUserByUsername(auth.getName()).getUsername());
        
        
        List lista = new ArrayList<String>();
        for(int i = 0; i<signupRepository.findAll().size();i++){
            lista.add(signupRepository.findAll().get(i).toString());
            
        }
        List messages = new ArrayList<String>();
        for(int i = 0; i<msg.findAll().size();i++){
            messages.add(msg.findAll().get(i).getMessage());
            
        }
        model.addAttribute("messages", messages);
        model.addAttribute("user", user.getUsername());
        model.addAttribute("signedup", lista);
        model.addAttribute("authlevel", user.getAuthority() );
        return "index";
    }
    
    @RequestMapping(value = "/searchSignup", method = RequestMethod.POST)
    public String searchSignup(Model model, @RequestParam String search){
        List lista = new ArrayList<String>();
        for(int i = 0; i<signupRepository.findAll().size();i++){
            
            lista.add(signupRepository.findByName(search));
            
        }
        model.addAttribute("search", lista);
        return "redirect:/index";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String loadRegister() {
        return "register";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        if (userDetailsService.register(username, encoder.encode(password))){
            return "redirect:/login";
        }
        return "register";
    }
    
    
    

}
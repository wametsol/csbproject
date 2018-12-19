package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import sec.project.domain.Message;

import sec.project.domain.Signup;
import sec.project.repository.MessageRepository;

import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private CustomUserDetailsService uds;
    @Autowired
    private MessageRepository msg;

    
    @RequestMapping(value = "/superSecret123form", method = RequestMethod.GET)
    public String loadForm(Authentication auth) {
        return "form";
    }

    @RequestMapping(value = "/superSecret123form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, @RequestParam String cardnumber) {
        signupRepository.save(new Signup(name, address, cardnumber));
        return "done";
    }
    
    @RequestMapping(value = "/addmessage", method = RequestMethod.POST)
    public String addMessage(Authentication auth, @RequestParam String message){
        
        msg.save(new Message(message));
        return "redirect:/index";
    }
    
    @RequestMapping(value = "/clearAllsignups", method = RequestMethod.GET)
    public String clearSignups(){
        signupRepository.deleteAll();
        msg.deleteAll();
        uds.init();
        return "redirect:/index";
    }
    
    

    


}

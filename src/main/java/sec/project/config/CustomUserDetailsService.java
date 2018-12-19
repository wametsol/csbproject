package sec.project.config;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sec.project.domain.Signup;
import sec.project.domain.User;
import sec.project.repository.SignupRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private SignupRepository signupRepository;
    
    private Map<String, User> accountDetails;

    @PostConstruct
    public void init() {
        // this data would typically be retrieved from a database
        this.accountDetails = new TreeMap<>();
        User newUser = new User("ted", "$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm");
        newUser.updateAuthority(2);
        this.accountDetails.put("ted", newUser);
        signupRepository.save(new Signup("Ted", "ted123@gmail.com", "123123123"));
        
        
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!this.accountDetails.containsKey(username)) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        if (username.matches("ted")){
            return new org.springframework.security.core.userdetails.User(
                username,
                this.accountDetails.get(username).getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("ALLOWED")));
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                this.accountDetails.get(username).getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
    
    public void changePassword(String username, String password){
        User newUser = new User(username, password);
        this.accountDetails.put(username, newUser);
    }
    
    public boolean register(String username, String password){
        if(this.accountDetails.containsKey(username)){
            return false;
        }
        User newUser = new User(username, password);
        this.accountDetails.put(username, newUser);
        return true;
    }
    public User getUser(String username){
        return this.accountDetails.get(username);
    }
    
    
}

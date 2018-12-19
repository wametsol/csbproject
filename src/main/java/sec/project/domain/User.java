package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class User extends AbstractPersistable<Long> {

    private String username;
    private String password;
    private int authority;

    public User() {
        super();
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
        this.authority = 0;
    }

    public String getUsername() {
        return username;
    }

    

    public String getPassword() {
        return password;
    }
    
    public int getAuthority(){
        return authority;
    }
    public void updateAuthority(int value){
        this.authority = value;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
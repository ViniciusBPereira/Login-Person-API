package group.spring.services.login.security.token;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import group.spring.services.login.model.Users;
import group.spring.services.login.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Check implements Authentication{

    private String username;
    private UsersRepository usersRepository;

    private Users get(){
        return usersRepository.findByUsername(username);
    }

    @Override
    public String getName() {
        return get().getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return get().getRoles();
    }

    @Override
    public Object getCredentials() {
        return get().getPassword();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return get();
    }

    @Override
    public boolean isAuthenticated() {
        if(get() != null){return true;}
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
    
}

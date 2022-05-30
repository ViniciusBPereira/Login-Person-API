package group.spring.services.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import group.spring.services.login.model.Users;
import group.spring.services.login.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDS implements UserDetailsService{

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username);
        log.info("The User " + user.getUsername() + " Login ");
        return new User(user.getUsername(), user.getPassword(), user.getRoles());
    }
    
}

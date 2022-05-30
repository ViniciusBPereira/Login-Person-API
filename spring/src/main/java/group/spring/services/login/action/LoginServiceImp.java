package group.spring.services.login.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import group.spring.services.login.model.Roles;
import group.spring.services.login.model.Users;
import group.spring.services.login.repository.RolesRepository;
import group.spring.services.login.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginServiceImp implements LoginService{

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Users saveUser(Users user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if(user.getRoles() == null){
            List<Roles> roles = new ArrayList<Roles>();
            roles.add(rolesRepository.findByName("ROLE_USER"));
            user.setRoles(roles);
        }
        log.info("Save User");
        return usersRepository.save(user);
    }

    @Override
    public Roles saveRole(Roles role) {
        log.info("Save Role");
        return rolesRepository.save(role);
    }

    @Override
    public Users getUser(Long id) {
        log.info("Get User");
        return usersRepository.getById(id);
    }

    @Override
    public List<Users> getUsers() {
        log.info("Get All Users");
        return usersRepository.findAll();
    }

    @Override
    public void addRoleToUser(String name, String username) {
        Roles role = rolesRepository.findByName(name);
        Users user = usersRepository.findByUsername(username);
        user.getRoles().add(role);
        
        log.info("Add Role To User");
        usersRepository.save(user);
    }
    
}

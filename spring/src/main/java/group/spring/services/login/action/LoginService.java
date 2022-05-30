package group.spring.services.login.action;

import java.util.List;

import group.spring.services.login.model.Roles;
import group.spring.services.login.model.Users;

public interface LoginService {
    public Users saveUser(Users user);
    public Roles saveRole(Roles role);
    public Users getUser(Long id);
    public List<Users> getUsers();
    public void addRoleToUser(String name, String username);
}

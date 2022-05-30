package group.spring.services.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.spring.services.login.action.Form;
import group.spring.services.login.action.LoginService;
import group.spring.services.login.model.Roles;
import group.spring.services.login.model.Users;

@RequestMapping(value = "/api/v1/login-service", produces = "application/json")
@RestController
public class LoginController {
    
    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/user")
    public ResponseEntity<Users> saveUser(@RequestBody Users user){
        return ResponseEntity.ok(loginService.saveUser(user));
    }

    @PostMapping(value = "/role")
    public ResponseEntity<Roles> saveRoles(@RequestBody Roles role){
        return ResponseEntity.ok(loginService.saveRole(role));
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<Users>> getUsers(){
        return ResponseEntity.ok(loginService.getUsers());
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Users> getUser(@PathVariable Long id){
        return ResponseEntity.ok(loginService.getUser(id));
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addRoleToUser(@RequestBody Form form){
        loginService.addRoleToUser(form.getName(), form.getUsername());
        return ResponseEntity.ok().build();
    }

}

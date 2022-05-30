package group.spring.services.login.model;

import lombok.Data;

@Data
public class Token {
    private String type_token;
    private String acess_token;
    private Long expire_token;
}

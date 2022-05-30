package group.spring.services.login.security.token;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import group.spring.services.login.model.Token;

public class Verify extends UsernamePasswordAuthenticationFilter{
    
    private final Long TOKEN_EXPIRE = 1800000l;

    public Verify(String url, AuthenticationManager authenticationManager){
        setFilterProcessesUrl(url);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        String tkn = JWT.create()
                        .withIssuer(request.getRequestURL().toString())
                        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRE))
                        .withSubject(authResult.getName())
                        .withAudience("frontend")
                        .sign(Algorithm.HMAC256("secret"));
        
        Token token = new Token();
        token.setType_token("Bearer");
        token.setAcess_token(tkn);
        token.setExpire_token(TOKEN_EXPIRE);

        response.getWriter().write(new ObjectMapper().writeValueAsString(token));
        response.getWriter().flush();
    }

}

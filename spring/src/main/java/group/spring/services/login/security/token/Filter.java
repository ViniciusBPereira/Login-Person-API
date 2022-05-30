package group.spring.services.login.security.token;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import group.spring.ApplicationContextLoad;
import group.spring.services.login.repository.UsersRepository;

public class Filter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(auth != null && auth.startsWith("Bearer ")){
            
            String tkn = auth.replace("Bearer ", "");

            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(tkn);

            String username = decodedJWT.getSubject();

            SecurityContextHolder.getContext().setAuthentication(new Check(username, ApplicationContextLoad.getAppContext().getBean(UsersRepository.class)));

            filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request, response);
        }

    }
    
}

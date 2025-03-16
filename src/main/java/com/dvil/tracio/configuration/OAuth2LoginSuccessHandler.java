package com.dvil.tracio.configuration;

import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.util.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        //String firstName = oauth2User.getAttribute("given_name");
        //String lastName = oauth2User.getAttribute("family_name");
        String accessToken = "";
        String refreshToken = "";

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail(email);
            //user.setFirstName(firstName);
            //user.setLastName(lastName);
            user.setUsername(email); // Or any other unique username logic
            user.setUserRole(RoleName.CYCLIST); // default role
            user.setUserPassword(encoder.encode("12345"));
            user.setPhone("123456789");
            //user.setAddress(null);
            accessToken = jwtService.generateAccessToken(user);
            refreshToken = jwtService.generateRefreshToken(user);
            user.setAccessToken(accessToken);
            user.setRefToken(refreshToken);
            userRepository.save(user);
        }
        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}");
        response.getWriter().flush();
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("http://localhost:5173");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

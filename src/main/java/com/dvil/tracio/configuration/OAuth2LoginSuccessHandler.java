package com.dvil.tracio.configuration;

import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.repository.UserRepo;
import com.dvil.tracio.service.EmailService;
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
import java.util.Random;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String accessToken = "";
        String refreshToken = "";

        User User = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        //authenValidation.ValidRegister(request);

        if (User != null) {
            // Nếu email đã được sử dụng, trả về lỗi 400
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Email đã được sử dụng. Vui lòng đăng nhập hoặc sử dụng email khác.\"}");
            response.getWriter().flush();
            return;
        } else {
            User user = new User();
            user.setEmail(email);
            user.setUsername(email); // Hoặc có thể sử dụng logic tạo username khác
            user.setRole(RoleName.CYCLIST); // Gán role mặc định là CYCLIST
            user.setUserPassword(encoder.encode("12345"));
            user.setPhone("123456789");

            // Tạo JWT Token
            accessToken = jwtService.generateAccessToken(user);
            refreshToken = jwtService.generateRefreshToken(user);
            user.setAccessToken(accessToken);
            user.setRefToken(refreshToken);

            // Gửi email xác thực
            String verifyCode = generateVerificationCode();
            emailService.sendVerifyCode(user, verifyCode, "Xác thực tài khoản", "Mã xác thực của bạn là: ");

            // Lưu user vào DB
            userRepository.save(user);

            // Trả về response JSON
            response.setContentType("application/json");
            response.getWriter().write("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}");
            response.getWriter().flush();

            this.setAlwaysUseDefaultTargetUrl(true);
            this.setDefaultTargetUrl("http://localhost:5173");
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // Mã 6 chữ số
    }
}

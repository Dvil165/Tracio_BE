package com.dvil.tracio;

import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import com.dvil.tracio.repository.RoleRepo;
import com.dvil.tracio.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TracioApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracioApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(RoleRepo roleRepo,
//                          UserRepo userRepo,
//                          PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (roleRepo.findByName(RoleName.ADMIN).isPresent()) return;
//            RoleName adminRole = roleRepo.save(RoleName.ADMIN);
//            roleRepo.save(RoleName.CYCLIST);
//
//            Set<RoleName> roles = new HashSet<>();
//
//            roles.add(adminRole);
//
//            User admin = new User(
//                    1,
//                    "act_admin",
//                    "rft_admin",
//                    UserVerifyStatus.Verified,
//                    Instant.now(),
//                    "admin@gmail.com",
//                    "123456789",
//                    passwordEncoder.encode("password"),
//                    RoleName.ADMIN,
//                    "admin",
//                    "rspt_admin",
//                    "vrft_admin"
//            );
//            userRepo.save(admin);
//        };
//    }

}

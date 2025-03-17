package com.dvil.tracio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

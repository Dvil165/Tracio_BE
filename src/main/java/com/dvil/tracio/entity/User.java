package com.dvil.tracio.entity;

import com.dvil.tracio.enums.RoleName;
import com.dvil.tracio.enums.UserVerifyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    // abcccc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "access_token", length = 1000, nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", length = 10000, nullable = false)
    private String refToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private UserVerifyStatus accountStatus = UserVerifyStatus.Unverified;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private RoleName role;  // Chỉ một role duy nhất

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "reset_password_token", length = 500)
    private String resetPasswordToken;

    @Column(name = "verification_code", length = 500)
    private String verificationCode;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = true) // User có thể thuộc shop hoặc không
    private Shop shop;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;  // Mặc định là true

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
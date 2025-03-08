package com.dvil.tracio.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenType {
    AccessToken,
    RefreshToken,
    ForgotPasswordToken,
    EmailVerificationToken
}

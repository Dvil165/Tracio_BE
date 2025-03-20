package com.dvil.tracio.service;

import com.dvil.tracio.request.AdminRegisterRequest;
import com.dvil.tracio.response.AdminRegisterResponse;

public interface AdminService {
    AdminRegisterResponse createAdminAccount(AdminRegisterRequest adminRegisterRequest);
}

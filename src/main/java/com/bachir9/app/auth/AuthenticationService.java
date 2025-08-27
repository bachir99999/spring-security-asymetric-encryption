package com.bachir9.app.auth;

import com.bachir9.app.auth.request.AuthenticationRequest;
import com.bachir9.app.auth.request.RefreshRequest;
import com.bachir9.app.auth.request.RegistrationRequest;
import com.bachir9.app.auth.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    void register(RegistrationRequest request);
    AuthenticationResponse refreshToken(RefreshRequest request);
}

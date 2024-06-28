package com.app.flex.tooling.service;

import com.app.flex.tooling.request.InputRequest;
import com.app.flex.tooling.response.Response;

public interface AuthenticationService {

	Response registerUser(InputRequest inputRequest);

	Response login(InputRequest inputRequest);

	Response generateOtp(InputRequest inputRequest);

	Response validateOtp(InputRequest inputRequest);

	Response updatedPassword(InputRequest inputRequest);

}

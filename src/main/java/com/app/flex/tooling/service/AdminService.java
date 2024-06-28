package com.app.flex.tooling.service;

import com.app.flex.tooling.request.InputRequest;
import com.app.flex.tooling.response.Response;

public interface AdminService {

	Response createInternalUser(InputRequest inputRequest);

	Response getUserList(InputRequest inputRequest);

}

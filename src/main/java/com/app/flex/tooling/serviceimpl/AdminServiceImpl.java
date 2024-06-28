package com.app.flex.tooling.serviceimpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.flex.tooling.constants.Appconstants;
import com.app.flex.tooling.helper.UserDetailsHelper;
import com.app.flex.tooling.models.Person;
import com.app.flex.tooling.models.Role;
import com.app.flex.tooling.models.SystemKey;
import com.app.flex.tooling.models.UserModel;
import com.app.flex.tooling.models.UserRole;
import com.app.flex.tooling.repository.PersonRepository;
import com.app.flex.tooling.repository.RoleRepository;
import com.app.flex.tooling.repository.SystemKeyRepository;
import com.app.flex.tooling.repository.UserRepo;
import com.app.flex.tooling.repository.UserRepository;
import com.app.flex.tooling.repository.UserRoleRepository;
import com.app.flex.tooling.request.InputRequest;
import com.app.flex.tooling.request.RequestData;
import com.app.flex.tooling.response.Response;
import com.app.flex.tooling.response.ResponseObject;
import com.app.flex.tooling.service.AdminService;
import com.app.flex.tooling.utils.CommonUtils;
import com.app.flex.tooling.utils.JwtServiceUtils;

@Service
public class AdminServiceImpl implements AdminService {

	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtServiceUtils jwtServiceUtils;

	@Autowired
	PersonRepository personRepository;

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	SystemKeyRepository systemKeyRepository;

	@Override
	public Response createInternalUser(InputRequest inputRequest) {
		RequestData requestData = inputRequest.getRequest().getRequestData();
		ResponseObject responseObject = new ResponseObject();
		String personType = requestData.getPersonType();
		try {
			Person isPersonExist = personRepository.findByMobileNoOrEmailAndIsActive(requestData.getMobileNo(),
					requestData.getEmail(), true);
			if (ObjectUtils.isEmpty(isPersonExist)) {
				SystemKey systemKey = systemKeyRepository.findByModule(personType.toUpperCase());
				String personId = null;
				if (!ObjectUtils.isEmpty(systemKey)) {
					personId = CommonUtils.getPersonId(systemKey.getPrefix(), systemKey.getSeries(),
							systemKey.getLength());
					systemKey.setSeries(systemKey.getSeries() + 1);
					systemKey.setUpdatedOn(new Date());
					systemKeyRepository.save(systemKey);
				}
				Person person = new Person();
				person.setPersonId(personId);
				person.setFirstName(requestData.getFirstName());
				person.setLastName(requestData.getLastName());
				person.setEmail(requestData.getEmail());
				person.setMobileNo(requestData.getMobileNo());
				person.setEmployeeId(requestData.getEmployeeId());
				person.setActive(true);
				person.setPersonType(personType);
				person.setCreatedOn(new Date());
				personRepository.save(person);
				UserModel user = new UserModel();
				user.setAccountNonExpired(true);
				user.setAccountNonLocked(true);
				user.setCredentialsNonExpired(true);
				user.setEnabled(true);
				user.setUsername(requestData.getEmail());
				user.setPassword(passwordEncoder.encode(requestData.getPassword()));
				user.setPersonId(person.getPersonId());
				userRepo.save(user);
				UserRole userRole = new UserRole();
				userRole.setAccessLevel(1);
				userRole.setUser(user);
				userRole.setCreatedBy(person.getPersonId());
				userRole.setCreatedOn(new Date());
				Role role = roleRepository.findByRoleId(personType.toUpperCase());
				userRole.setRole(role);
				userRoleRepository.save(userRole);
				var jwtToken = jwtServiceUtils.generateToken(user);
				responseObject.setToken(jwtToken);
				return new Response(true, Appconstants.STATUS_CODE_200, Appconstants.USER_ADDED_SUCCESSFULLY,
						responseObject);
			} else {
				return new Response(false, Appconstants.STATUS_CODE_209, Appconstants.ALREADY_EXIST);
			}
		} catch (Exception e) {
			logger.error("exception occur while processing createInternalUser()and error is :{}", e.getMessage());
			return new Response(false, Appconstants.STATUS_CODE_500, Appconstants.STATUS_CODE_500);
		}
	}

	@Override
	public Response getUserList(InputRequest inputRequest) {
		String searchValue = inputRequest.getRequest().getRequestData().getSearchValue();
		List<UserDetailsHelper> userDetailsList = null;
		ResponseObject responseObject = new ResponseObject();
		try {
			if (ObjectUtils.isEmpty(searchValue) || "All".equalsIgnoreCase(searchValue)) {
				userDetailsList = personRepository.getAllUserList(Arrays.asList(Appconstants.ROLES.split(",")));
			} else {
				userDetailsList = personRepository.getAllUserListByPersonType(searchValue);
			}
			if (!ObjectUtils.isEmpty(userDetailsList)) {
				responseObject.setUserDetailsList(userDetailsList);
				return new Response(true, Appconstants.STATUS_CODE_200, Appconstants.SUCCESS, responseObject);
			} else {
				return new Response(false, Appconstants.STATUS_CODE_209, Appconstants.NO_DATA_AVAILABLE);
			}
		} catch (Exception e) {
			logger.error("exception occur while processing getUserList() and error is :{}", e.getMessage());
			return new Response(false, Appconstants.STATUS_CODE_500, Appconstants.STATUS_CODE_500);
		}
	}

}

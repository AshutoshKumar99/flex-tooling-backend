package com.app.flex.tooling.serviceimpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.flex.tooling.constants.Appconstants;
import com.app.flex.tooling.helper.LoginHelper;
import com.app.flex.tooling.models.OtpModel;
import com.app.flex.tooling.models.Person;
import com.app.flex.tooling.models.Role;
import com.app.flex.tooling.models.SystemKey;
import com.app.flex.tooling.models.UserModel;
import com.app.flex.tooling.models.UserRole;
import com.app.flex.tooling.repository.OtpRepository;
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
import com.app.flex.tooling.service.AuthenticationService;
import com.app.flex.tooling.utils.CommonUtils;
import com.app.flex.tooling.utils.DateConvertor;
import com.app.flex.tooling.utils.JwtServiceUtils;
import com.app.flex.tooling.utils.OtpGenerator;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtServiceUtils jwtServiceUtils;

	@Autowired
	AuthenticationManager authenticationManager;

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

	@Autowired
	OtpRepository otpRepository;

	@Override
	public Response registerUser(InputRequest inputRequest) {
		RequestData requestData = inputRequest.getRequest().getRequestData();
		ResponseObject responseObject = new ResponseObject();
		try {
			Person isPersonExist = personRepository.findByMobileNoOrEmailAndIsActive(requestData.getMobileNo(),
					requestData.getEmail(), true);
			if (ObjectUtils.isEmpty(isPersonExist)) {
				SystemKey systemKey = systemKeyRepository.findByModule("CUSTOMER");
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
				person.setActive(true);
				person.setPersonType("Customer");
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
				Role role = roleRepository.findByRoleId("CUSTOMER");
				userRole.setRole(role);
				userRoleRepository.save(userRole);
				var jwtToken = jwtServiceUtils.generateToken(user);
				responseObject.setToken(jwtToken);
				return new Response(true, Appconstants.STATUS_CODE_200, Appconstants.REGISTRATION_SUCCESSFULL,
						responseObject);
			} else {
				return new Response(false, Appconstants.STATUS_CODE_209, Appconstants.ALREADY_EXIST);
			}
		} catch (Exception e) {
			logger.error("exception occur while processing registerUser()and error is :{}", e.getMessage());
			return new Response(false, Appconstants.STATUS_CODE_500, Appconstants.STATUS_CODE_500);
		}
	}

	@Override
	public Response login(InputRequest inputRequest) {
		RequestData requestData = inputRequest.getRequest().getRequestData();
		var userName = requestData.getUserName();
		var type = requestData.getType();
		var password = requestData.getPassword();
		ResponseObject responseObject = new ResponseObject();
		try {
			LoginHelper loginHelper = null;
			if ("MOBILE".equalsIgnoreCase(type)) {
				loginHelper = personRepository.getLoginDetailsByMobileNo(userName);
			} else if ("EMAIL".equalsIgnoreCase(type)) {
				loginHelper = personRepository.getLoginDetailsByEmail(userName);
			} else {
				return new Response(false, Appconstants.STATUS_CODE_409, "Please provide valid authentication type");
			}
			if (!ObjectUtils.isEmpty(loginHelper)) {
				var user = userRepository.findByUserId(loginHelper.getUserId());
				Authentication authenticated = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));
				if (authenticated.isAuthenticated()) {
					Map<String, Object> claims = new HashMap<>();
					claims.put("roleName", loginHelper.getRole());
					claims.put("firstName", loginHelper.getFirstName());
					claims.put("lastName", loginHelper.getLastName());
					claims.put("personId", loginHelper.getPersonId());
					claims.put("personMobile", loginHelper.getMobileNo());
					claims.put("email", loginHelper.getEmail());
					var jwtToken = jwtServiceUtils.generateToken(claims, user);
					responseObject.setToken(jwtToken);
					responseObject.setUserDetails(loginHelper);
					return new Response(true, Appconstants.STATUS_CODE_200, Appconstants.LOGIN_SUCCESSFULL,
							responseObject);
				} else {
					return new Response(false, Appconstants.STATUS_CODE_403, Appconstants.INVALID_CREDENTIALS);
				}
			} else {
				return new Response(false, Appconstants.STATUS_CODE_403, Appconstants.INVALID_CREDENTIALS);
			}
		} catch (Exception e) {
			logger.error("exception occur while processing login() and error is :{}", e.getMessage());
			return new Response(false, Appconstants.STATUS_CODE_500, Appconstants.STATUS_CODE_500);
		}
	}

	@Override
	public Response generateOtp(InputRequest inputRequest) {
		String mobileNo = inputRequest.getRequest().getRequestData().getMobileNo();
		ResponseObject responseObject = new ResponseObject();
		try {
			boolean isMobileExist = personRepository.existsByMobileNoAndIsActive(mobileNo, true);
			if (isMobileExist) {
				String otp = OtpGenerator.generateOTP(6);
				OtpModel otpDetails = new OtpModel();
				otpDetails.setOtp(otp);
				otpDetails.setMobileNo(mobileNo);
				otpDetails.setStatus("GENERATED");
				otpDetails.setCreatedOn(new Date());
				otpRepository.save(otpDetails);
				responseObject.setOtp(otp);
				return new Response(true, Appconstants.STATUS_CODE_200, Appconstants.SUCCESS, responseObject);
			} else {
				return new Response(false, Appconstants.STATUS_CODE_209, "User Not Register");
			}
		} catch (Exception e) {
			logger.error("exception occur while processing generateOtp() and error is :{}", e.getMessage());
			return new Response(false, Appconstants.STATUS_CODE_500, Appconstants.STATUS_CODE_500);
		}
	}

	@Override
	public Response validateOtp(InputRequest inputRequest) {
		RequestData requestData = inputRequest.getRequest().getRequestData();
		String mobileNo = requestData.getMobileNo();
		String otp = requestData.getOtp();
		try {
			OtpModel otpDetails = otpRepository.getLatestOtp(mobileNo, "GENERATED");
			if (!ObjectUtils.isEmpty(otpDetails)) {
				long mints = DateConvertor.dateDifferenceInMinutes(otpDetails.getCreatedOn());
				if (mints <= 15) {
					if (otpDetails.getOtp().equalsIgnoreCase(otp)) {
						otpDetails.setStatus("VALIDATED");
						otpDetails.setUpdatedOn(new Date());
						otpRepository.save(otpDetails);
						return new Response(true, Appconstants.STATUS_CODE_200, "Otp Validated Successfully");
					} else {
						return new Response(false, Appconstants.STATUS_CODE_209, "Invalid Otp");
					}
				} else {
					return new Response(false, Appconstants.STATUS_CODE_205, "Otp Expired");
				}
			} else {
				return new Response(false, Appconstants.STATUS_CODE_209, "Invalid Otp");
			}
		} catch (Exception e) {
			logger.error("exception occur while processing validateOtp() and error is :{}", e.getMessage());
			return new Response(false, Appconstants.STATUS_CODE_500, Appconstants.STATUS_CODE_500);
		}
	}

	@Override
	public Response updatedPassword(InputRequest inputRequest) {
		RequestData requestData = inputRequest.getRequest().getRequestData();
		String mobileNo = requestData.getMobileNo();
		String password = requestData.getPassword();
		try {
			String personId = personRepository.getPersonIdByMobileNo(mobileNo);
			if (!ObjectUtils.isEmpty(personId)) {
				String encryptedPassword = passwordEncoder.encode(password);
				int updatedCount = personRepository.updatePassword(encryptedPassword, personId);
				logger.info("updated count is :{}", updatedCount);
				return new Response(true, Appconstants.STATUS_CODE_200, "Password Updated Successfully");
			} else {
				return new Response(false, Appconstants.STATUS_CODE_209, "User Not Exist");
			}
		} catch (Exception e) {
			logger.error("exception occur while processing validateOtp() and error is :{}", e.getMessage());
			return new Response(false, Appconstants.STATUS_CODE_500, Appconstants.STATUS_CODE_500);
		}
	}
}

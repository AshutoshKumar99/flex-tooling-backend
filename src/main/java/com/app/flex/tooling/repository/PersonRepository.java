package com.app.flex.tooling.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.flex.tooling.helper.LoginHelper;
import com.app.flex.tooling.helper.UserDetailsHelper;
import com.app.flex.tooling.models.Person;

import jakarta.transaction.Transactional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

	Person findByMobileNoOrEmailAndIsActive(String mobileNo, String email, boolean isActive);

	@Query(value = "select p.person_id as personId,p.first_name as firstName,p.last_name as lastName, \r\n"
			+ "trim(concat(p.first_name,' ',p.last_name)) as name,\r\n"
			+ "p.email, p.mobile_no as mobileNo, p.employee_id as employeeId,\r\n"
			+ "u.user_id  as userId, u.\"password\" ,r.role_name as role\r\n"
			+ "from ft.person p inner join ft.users u \r\n" + "on p.person_id = u.person_id \r\n"
			+ "inner join ft.user_role ur on u.user_id = ur.user_id \r\n"
			+ "inner join ft.\"role\" r on ur.role_id = r.role_id \r\n"
			+ "where p.mobile_no =?1 and p.is_active =true", nativeQuery = true)
	LoginHelper getLoginDetailsByMobileNo(String userName);

	@Query(value = "select p.person_id as personId,p.first_name as firstName,p.last_name as lastName, \r\n"
			+ "trim(concat(p.first_name,' ',p.last_name)) as name,\r\n"
			+ "p.email, p.mobile_no as mobileNo, p.employee_id as employeeId,\r\n"
			+ "u.user_id  as userId, u.\"password\" ,r.role_name as role\r\n"
			+ "from ft.person p inner join ft.users u \r\n" + "on p.person_id = u.person_id \r\n"
			+ "inner join ft.user_role ur on u.user_id = ur.user_id \r\n"
			+ "inner join ft.\"role\" r on ur.role_id = r.role_id \r\n"
			+ "where p.email =?1 and p.is_active =true", nativeQuery = true)
	LoginHelper getLoginDetailsByEmail(String userName);

	@Query(value = "select p.person_id as personId,p.first_name as firstName,p.last_name as lastName, \r\n"
			+ "trim(concat(p.first_name,' ',p.last_name)) as name,\r\n"
			+ "p.email, p.mobile_no as mobileNo, p.employee_id as employeeId,\r\n"
			+ "u.user_id  as userId, u.\"password\" ,r.role_name as role\r\n"
			+ "from ft.person p inner join ft.users u \r\n" + "on p.person_id = u.person_id \r\n"
			+ "inner join ft.user_role ur on u.user_id = ur.user_id \r\n"
			+ "inner join ft.\"role\" r on ur.role_id = r.role_id \r\n"
			+ "where p.person_type IN ?1 and p.is_active =TRUE ORDER BY p.created_on desc", nativeQuery = true)
	List<UserDetailsHelper> getAllUserList(List<String> roleList);

	@Query(value = "select p.person_id as personId,p.first_name as firstName,p.last_name as lastName, \r\n"
			+ "trim(concat(p.first_name,' ',p.last_name)) as name,\r\n"
			+ "p.email, p.mobile_no as mobileNo, p.employee_id as employeeId,\r\n"
			+ "u.user_id  as userId, u.\"password\" ,r.role_name as role\r\n"
			+ "from ft.person p inner join ft.users u \r\n" + "on p.person_id = u.person_id \r\n"
			+ "inner join ft.user_role ur on u.user_id = ur.user_id \r\n"
			+ "inner join ft.\"role\" r on ur.role_id = r.role_id \r\n"
			+ "where p.person_type =?1 and p.is_active =TRUE ORDER BY p.created_on desc", nativeQuery = true)
	List<UserDetailsHelper> getAllUserListByPersonType(String searchValue);

	boolean existsByMobileNoAndIsActive(String mobileNo, boolean b);

	@Query(value = "select p.person_id  from ft.person p where p.mobile_no =?1 and p.is_active =true", nativeQuery = true)
	String getPersonIdByMobileNo(String mobileNo);

	@Transactional
	@Modifying
	@Query(value = "update ft.users set \"password\" =?1 where person_id =?2", nativeQuery = true)
	int updatePassword(String encryptedPassword, String personId);

}

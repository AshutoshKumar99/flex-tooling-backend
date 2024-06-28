package com.app.flex.tooling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.flex.tooling.models.OtpModel;

@Repository
public interface OtpRepository extends JpaRepository<OtpModel, Long> {

	@Query(value = "select * from ft.otp o where o.mobile_no=?1 and o.status=?2 order by created_on desc limit 1", nativeQuery = true)
	OtpModel getLatestOtp(String mobileNo, String string);

}

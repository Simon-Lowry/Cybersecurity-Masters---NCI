package com.spfwproject.quotes.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.AdminService;
import com.spfwproject.quotes.models.LockUserRequest;
import com.spfwproject.quotes.repositories.UserRepository;

@Component
public class AdminServiceImpl implements AdminService {
	@Autowired
	private final UserRepository userRepository;

	private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	public AdminServiceImpl(UserRepository userRepo) {
		this.userRepository = userRepo;
	}

	public UserEntity updateUserStatus(LockUserRequest lockUserRequest) {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);

		UserEntity userEntity = userRepository.findById(lockUserRequest.getUserId()).orElseThrow(RuntimeException::new);
		userEntity.setAccountLocked(lockUserRequest.isLockUser());

		userEntity = userRepository.save(userEntity);

		logger.info("Exiting method " + methodName + ".");
		return userEntity;
	}

}

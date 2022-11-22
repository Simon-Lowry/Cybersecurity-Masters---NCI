package com.spfwproject.quotes.interfaces;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.LockUserRequest;

public interface AdminService {
	public UserEntity updateUserStatus(LockUserRequest lockUserRequest);
}

package com.spfwproject.quotes.models;

public class LockUserRequest {	
	// lock or unlock a user account
	private boolean lockUser;
	private Long userId;
	
	public LockUserRequest(Long userId, boolean lockUser) {
		this.lockUser = lockUser;
		this.userId = userId;
	}
	
	public boolean isLockUser() {
		return lockUser;
	}
	
	public void setLockUser(boolean lockUser) {
		this.lockUser = lockUser;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "LockUserRequest [lockUser=" + lockUser + ", userId=" + userId + "]";
	}
}

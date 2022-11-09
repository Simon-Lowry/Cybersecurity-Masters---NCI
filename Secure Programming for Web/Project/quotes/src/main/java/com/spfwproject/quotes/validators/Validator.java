package com.spfwproject.quotes.validators;

import java.util.ArrayList;

public abstract class Validator {
	private boolean containsErrors = false;
	private ArrayList<String> listOfErrorsMessages;
	
	public abstract void validate();
	
	public boolean containsErrors() {
		return containsErrors;
	}
	
	public ArrayList<String> getListOfErrors() {
		return listOfErrorsMessages;
	}
	
	public void addErrorMessageToErrorList(String errorMessage) {
		if (listOfErrorsMessages == null) {
			listOfErrorsMessages = new ArrayList<String>();
			containsErrors = true;
		} 
		
		listOfErrorsMessages.add(errorMessage);	
	}
	
	public String toString() {
		String errorListMessage = "";
		
		if (containsErrors) {
			int count = 0;
			for (String error : listOfErrorsMessages) {
				errorListMessage += (count + 1) + ": " + error;
			}
			
			return errorListMessage;
		} else {
			return null;
		}
	}
}

package com.april.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return (userService.findUserByEmail(email) == null);
	}
}

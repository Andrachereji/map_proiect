package com.example.guiex1.domain;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getFirstName() == null || entity.getLastName() == null)
            throw new ValidationException("Names cannot be null");
        if(entity.getEmail() == null )
            throw new ValidationException("Email cannot be null");
        if(entity.getPassword() == null)
            throw new ValidationException("Password cannot be null");
    }
}

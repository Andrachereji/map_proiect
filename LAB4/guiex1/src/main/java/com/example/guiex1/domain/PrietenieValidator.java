package com.example.guiex1.domain;

import com.example.guiex1.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if (entity.getId() == null) throw new ValidationException("id cant be null");

        if (entity.getIdu1() == null) throw new ValidationException("the id for the first user cant null");

        if (entity.getIdu2() == null) throw new ValidationException("the id for the second user cant null");
    }
}


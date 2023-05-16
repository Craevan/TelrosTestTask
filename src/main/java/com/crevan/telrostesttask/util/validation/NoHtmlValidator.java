package com.crevan.telrostesttask.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {

    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        return s == null || Jsoup.isValid(s, Safelist.none());
    }
}

package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    Class<?> targetEntity();
    String joinTable();
    String joinColumn();        // Points to the "current" object's ID
    String inverseJoinColumn(); // Points to the "target" object's ID
}
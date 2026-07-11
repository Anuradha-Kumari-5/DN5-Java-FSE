package com.cognizant.springlearn.model;

/**
 * Simple POJO representing a country.
 *
 * This class itself has nothing Spring-specific about it -- it's a plain
 * Java object with two fields, getters, and setters. The "India" instance
 * of this class is created and configured entirely from beans.xml (see
 * src/main/resources/beans.xml), not from annotations here. Spring reads
 * that XML, instantiates a Country object, calls setCode("IN") and
 * setName("India") on it (matching the <property> tags), and stores the
 * result in the application context under the bean id "india".
 */
public class Country {

    private String code;
    private String name;

    public Country() {
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

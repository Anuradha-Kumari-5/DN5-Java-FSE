package com.cognizant.ormlearn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistence class mapping to the "country" table.
 *
 * @Entity tells Spring Data JPA this class represents a row in a
 * database table. @Table pins down exactly which table ("country").
 * @Id marks the primary key field, and each @Column ties a Java field
 * to a specific database column name -- this indirection means the
 * Java field name and the DB column name don't have to match exactly
 * (here they do, but it's what lets them diverge if needed).
 */
@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "co_code")
    private String code;

    @Column(name = "co_name")
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

    @Override
    public String toString() {
        return "Country [code=" + code + ", name=" + name + "]";
    }

}

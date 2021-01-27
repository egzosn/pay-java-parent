package com.egzosn.pay.paypal.v2.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * The name of the party.
 */
public class Name {

    // Required default constructor
    public Name() {
    }

    /**
     * DEPRECATED. The party's alternate name. Can be a business name, nickname, or any other name that cannot be split into first, last name. Required when the party is a business.
     */
    @JSONField(name = "alternate_full_name")
    private String alternateFullName;

    public String alternateFullName() {
        return alternateFullName;
    }

    public Name alternateFullName(String alternateFullName) {
        this.alternateFullName = alternateFullName;
        return this;
    }

    /**
     * When the party is a person, the party's full name.
     */
    @JSONField(name = "full_name")
    private String fullName;

    public String fullName() {
        return fullName;
    }

    public Name fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    /**
     * When the party is a person, the party's given, or first, name.
     */
    @JSONField(name = "given_name")
    private String givenName;

    public String givenName() {
        return givenName;
    }

    public Name givenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    /**
     * When the party is a person, the party's middle name. Use also to store multiple middle names including the patronymic, or father's, middle name.
     */
    @JSONField(name = "middle_name")
    private String middleName;

    public String middleName() {
        return middleName;
    }

    public Name middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    /**
     * The prefix, or title, to the party's name.
     */
    @JSONField(name = "prefix")
    private String prefix;

    public String prefix() {
        return prefix;
    }

    public Name prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * The suffix for the party's name.
     */
    @JSONField(name = "suffix")
    private String suffix;

    public String suffix() {
        return suffix;
    }

    public Name suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * When the party is a person, the party's surname or family name. Also known as the last name. Required when the party is a person. Use also to store multiple surnames including the matronymic, or mother's, surname.
     */
    @JSONField(name = "surname")
    private String surname;

    public String surname() {
        return surname;
    }

    public Name surname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getAlternateFullName() {
        return alternateFullName;
    }

    public void setAlternateFullName(String alternateFullName) {
        this.alternateFullName = alternateFullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}

package com.ws.jg.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author Sudiptasish Chanda
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Player implements Serializable {

    private String id;
    private String name;
    private String password;
    private String email;
    private String contact;
    private String address;
    private int chairId = -1;
    private Date createDate;
    
    public Player() {}

    public Player(String id, String name, String password, String email,
            String contact, String address, Date createDate) {
        
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.createDate = createDate;
    }

    /**
     * @return the playerId
     */
    public String getId() {
        return id;
    }

    /**
     * @param playerId the playerId to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the playerName
     */
    public String getName() {
        return name;
    }

    /**
     * @param playerName the playerName to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the chair id
     */
    public int getChairId() {
        return chairId;
    }

    /**
     * @param chairId the chairId to set
     */
    public void setChairId(int chairId) {
        this.chairId = chairId;
    }
}

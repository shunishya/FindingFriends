/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.findingfriends.api.models;

import java.util.ArrayList;

import com.findingfriends.models.UserWithDistance;

/**
 *
 * @author nitu
 */
public class GroupOfFriendsResponse {
    ArrayList<UserWithDistance> groupOfPeople;

    public ArrayList<UserWithDistance> getGroupOfPeople() {
        return groupOfPeople;
    }

    public void setGroupOfPeople(ArrayList<UserWithDistance> groupOfPeople) {
        this.groupOfPeople = groupOfPeople;
    }
    
}

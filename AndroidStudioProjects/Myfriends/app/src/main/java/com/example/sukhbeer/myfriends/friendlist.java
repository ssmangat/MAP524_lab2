package com.example.sukhbeer.myfriends;

/**
 * Created by sukhbeer on 10/08/15.
 */
public class friendlist {
    private String name;
    private String address;
    private String phone;

    public void friendlist(String name,String address,String phone){
        this.phone=phone;
        this.address=address;
        this.name=name;
    }
    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getPhone(){
        return phone;
    }

    public void setName(String name){
        this.name=name;
    }

    public  void  setAddress(String address){
        this.address=address;
    }

    public  void  setPhone(String phone){
        this.phone=phone;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

package com.example.project4_test1.HomeFragment;

//How to use ======================================================
// AEDinfo temp = new AEDinfo().setItem(address, tel, lat, lng);
//=================================================================

public class AEDinfo {
    public String address;
    public String tel;
    public double lat;
    public double lng;
    public String error = "success";


    public AEDinfo(){}

    public AEDinfo setItem(String address, String tel, double lat, double lng){
        AEDinfo result = new AEDinfo();
        result.address = address;
        result.tel = tel;
        result.lat = lat;
        result.lng = lng;

        return result;
    }

    public AEDinfo setError(){
        AEDinfo result = new AEDinfo();
        result.error = "error";

        return result;
    }


}

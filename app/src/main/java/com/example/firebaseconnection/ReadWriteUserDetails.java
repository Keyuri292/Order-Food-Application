package com.example.firebaseconnection;

public class ReadWriteUserDetails
{
    public String EmailId, MobileNo, Password;

    public ReadWriteUserDetails(String textemailid, String textmobileno, String textpassword)
    {
        this.EmailId = textemailid;
        this.MobileNo = textmobileno;
        this.Password = textpassword;
    }
}

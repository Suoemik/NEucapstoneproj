package com.example.tortuga.myapplication;

/**
 * Created by Tortuga on 2/15/16.
 */

public class Contact {

    String name , email , username , password;

    public Contact(String name , String email , String username , String password)
    {
        this.name = name;
        this.email = email ;
        this.username = username;
        this.password = password;
    }

    public Contact(String username , String password)
    {
        this.username = username;
        this.password = password;
    }
}


/*
public class Contact {

    String name, email, username, password;


    public Contact (String name, String email, String username, String password)
    {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Contact (String username, String password)
    {
        this.username = username;
        this.password = password;
    }


    public void setName (String name)
    {
        this.name = name;
    }

    public String getName ()
    {
        return this.name;
    }


    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getEmail ()
    {
        return this.email;
    }

    public void setUname (String uname)
    {
        this.username = uname;
    }

    public String getuname ()
    {
        return this.username;
    }

    public void setPass (String pass)
    {
        this.password = pass;
    }

    public String getPass ()
    {
        return this.password;
    }
*/



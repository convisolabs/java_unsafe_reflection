package com.testcases.serial;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.*;


class Demo implements java.io.Serializable
{
    public int a;
    public String b;

    public Demo(int a, String b) {
        this.a = a;
        this.b = b;
    }
}


public class VulnDeser1 {

    public VulnDeser1(String str) {
        this.vuln1(str);
    }

    public void vuln1(String arg) {
        try {
            FileInputStream file = new FileInputStream(arg);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Demo object1 = (Demo)in.readObject();
            in.close();
            file.close();

        } catch(IOException ex) {
        } catch(ClassNotFoundException ex) {}
    }

}
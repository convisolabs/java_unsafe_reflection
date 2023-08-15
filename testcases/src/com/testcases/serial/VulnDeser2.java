package com.testcases.serial;

import java.beans.XMLDecoder;
import java.io.*;

class MyClass implements Serializable {
    private String prop;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}

public class VulnDeser2 {

    public VulnDeser2(String str) {
        MyClass obj = this.fromString(str);
        System.out.println("obj.getProp() = " + obj.getProp());
    }

    public MyClass fromString(String arg) {
        XMLDecoder d = new XMLDecoder(new ByteArrayInputStream(arg.getBytes()));
        MyClass obj = (MyClass)d.readObject();
        d.close();
        return obj;
    }
}

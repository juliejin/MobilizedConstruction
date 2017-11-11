package com.mobilizedconstruction.model;

/**
 * Created by Ling Jin on 08/11/2017.
 */

public class DeptTable {
    int no;
    String name;

    public DeptTable(int no, String name) {
        super();
        this.no = no;
        this.name = name;
    }

    public DeptTable() {
        super();
        this.no=0;
        this.name = null;
    }

    public int getNo() {
        return no;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

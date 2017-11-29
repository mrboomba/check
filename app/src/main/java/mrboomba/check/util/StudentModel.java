package mrboomba.check.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mrboomba on 29/11/2560.
 */

public class StudentModel {
    private String firstName,lastName;
    private int id,classAmnt,assignAmnt;
    private int[] classCheck,assignCheck;

    public StudentModel(String firstName, String lastName, int id, int classAmnt, int assignAmnt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.classAmnt = classAmnt;
        this.assignAmnt = assignAmnt;
        classCheck = new int[classAmnt];
        assignCheck = new int[assignAmnt];
        Arrays.fill(classCheck,-1);
        Arrays.fill(assignCheck,-1);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassChecked(int week) {
        return classCheck[week-1];
    }

    public int getAssignChecked(int week) {
        return assignCheck[week-1];
    }

    public void checkClass(int week,int value) {
        classCheck[week-1] = value;
    }
    public void checkAssign(int week,int value) {
        assignCheck[week-1] = value;
    }

    public int getClassAmnt() {
        return classAmnt;
    }

    public void setClassAmnt(int classAmnt) {
        this.classAmnt = classAmnt;
        int[] tmp = classCheck;
        classCheck = new int[classAmnt];
        for(int i=0;i<tmp.length;i++){
            classCheck[i] = tmp[i];
        }
        classCheck[tmp.length] = -1;

    }

    public int getAssignAmnt() {
        return assignAmnt;
    }

    public void setAssignAmnt(int assignAmnt) {
        this.assignAmnt = assignAmnt;
        int[] tmp = assignCheck;
        assignCheck = new int[assignAmnt];
        for(int i=0;i<tmp.length;i++){
            assignCheck[i] = tmp[i];
        }
        assignCheck[tmp.length] = -1;
    }
}

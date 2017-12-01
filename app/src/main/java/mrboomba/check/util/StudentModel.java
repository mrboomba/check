package mrboomba.check.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mrboomba on 29/11/2560.
 */

public class StudentModel implements Parcelable{
    private String firstName,lastName,id;
    private int classAmnt,assignAmnt;
    private int[] classCheck,assignCheck;

    public StudentModel(){

    }

    public StudentModel(String firstName, String lastName, String id, int classAmnt, int assignAmnt) {
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

    protected StudentModel(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        id = in.readString();
        classAmnt = in.readInt();
        assignAmnt = in.readInt();
        classCheck = in.createIntArray();
        assignCheck = in.createIntArray();
    }

    public static final Creator<StudentModel> CREATOR = new Creator<StudentModel>() {
        @Override
        public StudentModel createFromParcel(Parcel in) {
            return new StudentModel(in);
        }

        @Override
        public StudentModel[] newArray(int size) {
            return new StudentModel[size];
        }
    };

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getClassChecked(int week) {
        return classCheck[week];
    }

    public int getAssignChecked(int week) {
        return assignCheck[week];
    }

    public void checkClass(int week,int value) {
        classCheck[week] = value;
    }
    public void checkAssign(int week,int value) {
        assignCheck[week] = value;
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
        int[] tmp2 = assignCheck.clone();
        assignCheck = new int[assignAmnt];
        Log.d("mrboomba",tmp2.length+" "+assignCheck.length);
        for(int i=0;i<tmp2.length;i++){
            assignCheck[i] = tmp2[i];
        }
        assignCheck[tmp2.length] = -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(id);
        parcel.writeInt(classAmnt);
        parcel.writeInt(assignAmnt);
        parcel.writeIntArray(classCheck);
        parcel.writeIntArray(assignCheck);
    }
}

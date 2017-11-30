package mrboomba.check.util;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mrboomba on 29/11/2560.
 */

public class ClassModel implements Parcelable {
    private StudentModel[] students;
    private int studentAmnt, classAmnt, assignAmnt, currentWeek, currentAssign;

    FileUtil fileUtil;
    String className;
    private File file;
    private String fileName;
    private Sheet sheet;
    private Workbook workbook;

    public ClassModel(String filename) {
        this.fileName = filename;
        file = new File(fileName);
        FileInputStream fIP = null;
        try {
            fIP = new FileInputStream(file);
            //Get the workbook instance for XLSX file
            String[] tmp = fileName.split("\\.");
            if (tmp[tmp.length - 1].equalsIgnoreCase("xls"))
                workbook = new HSSFWorkbook(fIP);
            else
                workbook = new XSSFWorkbook(fIP);
            sheet = workbook.getSheetAt(0);

            Row row = sheet.getRow(sheet.getLastRowNum());

            Cell cell = row.getCell(0);
            if (cell == null) cell = row.createCell(0);
            studentAmnt = (int) cell.getNumericCellValue();

            cell = row.getCell(1);
            classAmnt = (int) cell.getNumericCellValue();

            cell = row.getCell(2);
            assignAmnt = (int) cell.getNumericCellValue();

            cell = row.getCell(3);
            currentWeek = (int) cell.getNumericCellValue();

            cell = row.getCell(4);
            currentAssign = (int) cell.getNumericCellValue();
            students = new StudentModel[studentAmnt];

            for (int i = 0; i < studentAmnt; i++) {
                row = sheet.getRow(i);
                cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String id = cell.getStringCellValue();

                cell = row.getCell(1);
                String[] tmp1 = cell.getStringCellValue().split(" ");
                String firstName = tmp1[0];
                String lastName = tmp1[1];

                students[i] =new StudentModel(firstName, lastName, id, classAmnt, classAmnt);
            }

            for (int i = 0; i < studentAmnt; i++) {
                row = sheet.getRow(i);
                for (int j = 0, k = 2; j < classAmnt; j++, k++) {
                    cell = row.getCell(k);
                    students[i].checkClass(j, (int) cell.getNumericCellValue());
                }

                for (int j = 0, k = 2+classAmnt; j < assignAmnt; j++, k++) {
                    cell = row.getCell(k);
                    students[i].checkAssign(j, (int) cell.getNumericCellValue());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ClassModel(String filename,String className, int classAmnt, int assignAmnt) {
        fileUtil = new FileUtil();

        String[] nametmp = filename.split("/");
        fileName =filename.replace(nametmp[nametmp.length-1],className);
        fileName = fileName.replace("import","working");
        nametmp = filename.split("\\.");
        fileName = fileName.concat("."+nametmp[nametmp.length-1]);
        fileUtil.copyFile(filename,fileName);

        file = new File(fileName);
        this.classAmnt = classAmnt;
        this.assignAmnt = assignAmnt;
        studentAmnt = 0;
        this.className = className;

        FileInputStream fIP = null;
        try {
            fIP = new FileInputStream(file);
            //Get the workbook instance for XLSX file
            String[] tmp = fileName.split("\\.");
            if (tmp[tmp.length - 1].equalsIgnoreCase("xls"))
                workbook = new HSSFWorkbook(fIP);
            else
                workbook = new XSSFWorkbook(fIP);
            sheet = workbook.getSheetAt(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        initModel();

    }

    protected ClassModel(Parcel in) {
        students = in.createTypedArray(StudentModel.CREATOR);
        studentAmnt = in.readInt();
        classAmnt = in.readInt();
        assignAmnt = in.readInt();
        currentWeek = in.readInt();
        currentAssign = in.readInt();
        fileName = in.readString();

        file = new File(fileName);
        FileInputStream fIP = null;
        try {
            fIP = new FileInputStream(file);
            //Get the workbook instance for XLSX file
            String[] tmp = fileName.split("\\.");
            if (tmp[tmp.length - 1].equalsIgnoreCase("xls"))
                workbook = new HSSFWorkbook(fIP);
            else
                workbook = new XSSFWorkbook(fIP);
            sheet = workbook.getSheetAt(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<ClassModel> CREATOR = new Creator<ClassModel>() {
        @Override
        public ClassModel createFromParcel(Parcel in) {
            return new ClassModel(in);
        }

        @Override
        public ClassModel[] newArray(int size) {
            return new ClassModel[size];
        }
    };

    public void initModel() {
        ArrayList<StudentModel> students = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String id = cell.getStringCellValue();

            cell = row.getCell(1);
            String[] tmp = cell.getStringCellValue().split(" ");
            String firstName = tmp[0];
            String lastName = tmp[1];

            students.add(new StudentModel(firstName, lastName, id, classAmnt, classAmnt));
            studentAmnt++;

        }
        this.students = new StudentModel[studentAmnt];
        for(int i =0;i<students.size();i++){
            this.students[i] = students.get(i);
        }
        writeCheckedValue();
    }

    public void writeCheckedValue() {
        sheet = workbook.getSheetAt(0);
        try {
            for (int i = 0; i < studentAmnt; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0, k = 2; j < classAmnt; j++, k++) {
                    Cell cell = row.getCell(k);
                    if (cell == null) cell = row.createCell(k);
                    cell.setCellValue(students[i].getClassChecked(j));
                }

                for (int j = 0, k = 2+classAmnt; j < assignAmnt; j++, k++) {
                    Cell cell = row.getCell(k);
                    if (cell == null) cell = row.createCell(k);
                    cell.setCellValue(students[i].getAssignChecked(j));
                }
            }
            Row row = sheet.getRow(studentAmnt);
            if(row==null) row = sheet.createRow(studentAmnt);

            Cell cell = row.getCell(0);
            if (cell == null) cell = row.createCell(0);
            cell.setCellValue(studentAmnt);

            cell = row.getCell(1);
            if (cell == null) cell = row.createCell(1);
            cell.setCellValue(classAmnt);

            cell = row.getCell(2);
            if (cell == null) cell = row.createCell(2);
            cell.setCellValue(assignAmnt);

            cell = row.getCell(3);
            if (cell == null) cell = row.createCell(3);
            cell.setCellValue(currentWeek);

            cell = row.getCell(4);
            if (cell == null) cell = row.createCell(4);
            cell.setCellValue(currentAssign);

            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            outputStream.close();
            String[] tmp = fileName.split("\\.");
            FileInputStream fIP = new FileInputStream(file);
            if(!tmp[tmp.length-1].equalsIgnoreCase("xls"))
                workbook = new XSSFWorkbook(fIP);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Todo: Write per value
    public void writeCheckedValue(int week) {
        try {
            for (int i = 0; i < studentAmnt; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0, k = 2; j < classAmnt; j++, k++) {
                    Cell cell = row.getCell(k);
                    if (cell == null) cell = row.createCell(j);
                    cell.setCellValue(students[i].getClassChecked(j));
                }

                for (int j = 0, k = 2+studentAmnt; j < assignAmnt; j++, k++) {
                    Cell cell = row.getCell(k);
                    if (cell == null) cell = row.createCell(j);
                    cell.setCellValue(students[i].getAssignChecked(j));
                }

                FileOutputStream outputStream = new FileOutputStream(fileName);
                workbook.write(outputStream);
                outputStream.close();
                String[] tmp = fileName.split("\\.");
                FileInputStream fIP = new FileInputStream(file);
                if(!tmp[tmp.length-1].equalsIgnoreCase("xls"))
                    workbook = new XSSFWorkbook(fIP);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedArray(students,0);
        parcel.writeInt(studentAmnt);
        parcel.writeInt(classAmnt);
        parcel.writeInt(assignAmnt);
        parcel.writeInt(currentWeek);
        parcel.writeInt(currentAssign);
        parcel.writeString(fileName);
    }

    public StudentModel[] getStudents() {
        return students;
    }

    public void setStudents(StudentModel[] students) {
        this.students = students;
    }

    public int getStudentAmnt() {
        return studentAmnt;
    }

    public void setStudentAmnt(int studentAmnt) {
        this.studentAmnt = studentAmnt;
    }

    public int getClassAmnt() {
        return classAmnt;
    }

    public void setClassAmnt(int classAmnt) {
        this.classAmnt = classAmnt;
        for(int i =0;i<studentAmnt;i++){
            students[i].setClassAmnt(classAmnt);
        }
    }

    public int getAssignAmnt() {
        return assignAmnt;
    }

    public void setAssignAmnt(int assignAmnt) {
        this.assignAmnt = assignAmnt;
        for(int i =0;i<studentAmnt;i++){
            students[i].setAssignAmnt(assignAmnt);
        }
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public int getCurrentAssign() {
        return currentAssign;
    }

    public void setCurrentAssign(int currentAssign) {
        this.currentAssign = currentAssign;
    }
}

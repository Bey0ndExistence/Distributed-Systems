package beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties
public class StudentList{
    @JacksonXmlElementWrapper(localName="student_list")
    private ArrayList<StudentBean> students;

    public ArrayList<StudentBean> getStudent_list() {
        return students;
    }

    public void setStudent_list(ArrayList<StudentBean> student_list) {
        this.students = student_list;
    }
}
package Utilities;

import beans.StudentBean;

public class StudentHtmlGenerator {

    public String serialize(StudentBean s){
        String html = "";
        html += "<ul type=\"bullet\">\n" +
                "    <li>Nume: " +s.getNume() + "</li>\n" +
                "    <li>Preume: " +s.getPrenume() + "</li>\n" +
                "    <li>Varsta: " +s.getVarsta() + "</li>\n" +
                "</ul>";
        return html;
    }
}

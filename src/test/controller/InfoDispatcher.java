
package test.controller;

import java.util.ArrayList;
import test.model.StudentCollection;
import test.model.StudentInfo;

/**
 *
 * @author chenjiaxin
 */
public class InfoDispatcher {

    StudentCollection collection = new StudentCollection();

    /**
     *
     * @param predictedLabel
     * @return
     */
    public ArrayList<String> afterRecog(int predictedLabel) {
        
        ArrayList<StudentInfo> students = collection.getAllStudents(predictedLabel);
        ArrayList<String> result = new ArrayList<>();
        //System.out.println("get students array ok");
        boolean found = false;
        for (StudentInfo s : students) {
            if (s.getLabel() == predictedLabel) {
                display(s);
                found = true;
                result.add(s.getName());
                result.add(s.getGender());
                result.add(s.getProgram());
                result.add(Integer.toString(s.getAge()));
                result.add(s.getLastVisited().toString());
                result.add(s.getReason());
                //get(6)
                result.add(s.getPhoto());
                result.add(s.getAndrewId());
                //System.out.println("result1: "+result.get(1));
                return result;
            }
        }
        return result;
    }

    //display student info in back platform

    /**
     *
     * @param s
     * @return
     */
    public String display(StudentInfo s) {
        //System.out.println("-----------------------------------------------------------------");
        //System.out.println("This is student: " + s.getName());
        //System.out.println("Basic information:\n");
        //System.out.println(s.toString());
        return s.toString();
    }

}

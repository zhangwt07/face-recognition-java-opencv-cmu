
package test.model;

import java.util.Date;

/**
 *
 * @author chenjiaxin
 */
public class StudentInfo {
    
    // unchangeable fields 
    private final String name;
    private final String andrewId;
  
    private int age;
    private String program;
    
    // the path of photo
    private String photo;

    // when the student visited reception last time;
    private Date lastVisited;
    // how many times the student visited reception
    private int count;
    // why visited reception
    private String reason;

    // recoginition label
    private int label;
    private String gender;

    /**
     *
     * @param name
     * @param andrewId
     * @param age
     * @param program
     * @param photo
     * @param lastVisited
     * @param count
     * @param reason
     * @param label
     */
    public StudentInfo(String name, String andrewId, int age, String program, String photo, Date lastVisited,
            int count, String reason, int label, String gender) {
        this.name = name;
        this.andrewId = andrewId;
        this.age = age;
        this.program = program;
        this.photo = photo;
        this.lastVisited = lastVisited;
        this.count = count;
        this.reason = reason;
        this.label = label;
        this.gender = gender;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return andrewId
     */
    public String getAndrewId() {
        return this.andrewId;
    }

    /**
     *
     * @return age
     */
    public int getAge() {
        return this.age;
    }

    /**
     *
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     *
     * @return program
     */
    public String getProgram() {
        return this.program;
    }

    /**
     *
     * @param program
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     *
     * @return photo
     */
    public String getPhoto() {
        return this.photo;
    }

    /**
     *
     * @param photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     *
     * @return lastVisited
     */
    public Date getLastVisited() {
        return this.lastVisited;
    }

    /**
     *
     * @param day
     */
    public void setLastVisited(Date day) {
        this.lastVisited = day;
    }

    /**
     *
     * @return count
     */
    public int getCount() {
        return this.count;
    }

    /**
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @return reason
     */
    public String getReason() {
        return this.reason;
    }

    /**
     *
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     *
     * @return label
     */
    public int getLabel() {
        return this.label;
    }

    /**
     *
     * @param label
     */
    public void setLabel(int label) {
        this.label = label;
    }
    
    public String getGender(){
        return this.gender;
    }
    
    public void setGender(String gender){
        this.gender = gender;
    }

    public String toString() {
        String begin = "\n\n\n\n******************Student Infomation**************\n\n";
        String basic = "*Name:  " + this.getName() + "\n\n"
                + "*AndrewId:  " + this.getAndrewId() + "\n\n"
                + "*Age:  " + this.getAge() + "\n\n"
                + "*Program:  " + this.getProgram() + "\n\n";

        String visitInfo = "\n*Last visit time:  " + this.getLastVisited() + "\n\n"
                + "*Visit times:  " + this.getCount() + "\n\n"
                + "*Visit reason:  " + this.getReason() + "\n\n"
                + "*Gender:  " + this.getGender() + "\n\n";
        String end = "\n--------------------------------------------------------";

        return begin + basic + visitInfo + end;
    }

}

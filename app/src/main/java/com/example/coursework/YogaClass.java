package com.example.coursework;

public class YogaClass {
    private int id;
    private String date;
    private String comment;
    private String teacher;

    private int courseId;

    public YogaClass(int id, int courseId, String date, String comment, String teacher) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.courseId = courseId;
        this.teacher = teacher;
    }

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}

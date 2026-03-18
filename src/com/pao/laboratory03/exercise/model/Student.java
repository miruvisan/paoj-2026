package com.pao.laboratory03.exercise.model;

import com.pao.laboratory03.exercise.exception.InvalidGradeException;
import com.pao.laboratory03.exercise.exception.InvalidStudentException;
import java.util.*;

public class Student {
    private String name;
    private int age;
    private Map<Subject, Double> grades;

    public Student(String name, int age) {
        if (age < 18 || age > 60) {
            throw new InvalidStudentException("Varsta " + age + " este invalida (18-60)");
        }
        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }

    public void addGrade(Subject subject, double grade) {
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException("Nota " + grade + " trebuie sa fie intre 1 și 10");
        }
        this.grades.put(subject, grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) return 0.0;
        double sum = 0;
        for (double g : grades.values()) sum += g;
        return sum / grades.size();
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public Map<Subject, Double> getGrades() { return grades; }

    @Override
    public String toString() {
        return String.format("Student{name='%s', age=%d, avg=%.2f}", name, age, getAverage());
    }
}
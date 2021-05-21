package ru.hse.java.repetinder;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;
import java.util.List;

import ru.hse.java.repetinder.exception.MatchingAlgorithmException;

/**
 * This is a class which find tutors for students
 */
public class MatchingAlgorithm {

    private final List<Tutor> tutors;
    //private final List<Student> students;

    MatchingAlgorithm(List<Tutor> tutors) { // List<Student> students) {
        this.tutors = tutors;
        //this.students = students;
    }

    /**
     * Find the most suitable tutor for curStudent
     *
     * First algorithm choose only tutors that have minPrice lower than
     * minPrice of Student. Than we choose Tutor that have the highest
     * amount of points from not chosen Tutors
     *
     * @param curStudent for whom find Tutor
     * @return the most suitable tutor or null if there is no tutors
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Tutor showNextUser(Student curStudent) {
        return tutors.stream()
                .filter(tutor -> !curStudent.isTutorApproved(tutor))
                .filter(tutor -> !curStudent.isTutorSkipped(tutor))
                .filter(tutor -> tutor.getMinPrice() <= curStudent.getMaxPrice())
                .max(Comparator.comparingInt(Tutor::getRang))
                .orElse(null);
    }

    /**
     * Function that increases tutor's rang, because student
     * choose this tutor and swipe this tutor right.
     *
     * Throws MatchingAlgorithmException if tutor was swiped by student.
     *
     * @param student who swipe
     * @param tutor who was chosen by student
     */
    public void swipeRight(Student student, Tutor tutor) throws MatchingAlgorithmException {
        if (student.isTutorSkipped(tutor) || student.isTutorApproved(tutor)) {
            throw new MatchingAlgorithmException("Tutor was swiped by student");
        }
        student.addTutorToApproved(tutor);
        tutor.addStudentToList(student);
        tutor.increaseRang();
    }

    /**
     * Function that decreases tutor's rang, because student
     * doesn't choose this tutor and swipe this tutor left
     *
     * Throws MatchingAlgorithmException if tutor was swiped by student.
     *
     * @param student who swipe
     * @param tutor who wasn't chosen by student
     */
    public void swipeLeft(Student student, Tutor tutor) throws MatchingAlgorithmException {
        if (student.isTutorSkipped(tutor) || student.isTutorApproved(tutor)) {
            throw new MatchingAlgorithmException("Tutor was swiped by student");
        }
        student.addTutorToSkipped(tutor);
        tutor.decreaseRang();
    }
}

package ru.hse.java.repetinder;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ru.hse.java.repetinder.matchingAlgorithm.MatchingAlgorithm;
import ru.hse.java.repetinder.exception.MatchingAlgorithmException;
import ru.hse.java.repetinder.user.Student;
import ru.hse.java.repetinder.user.Tutor;
import ru.hse.java.repetinder.user.UserRepetinder;

public class MatchingAlgorithmTest {

    private Student getOneStudent() {
        return new Student("0", "German", "German", "geh4@gmail.com", UserRepetinder.Subject.CHEMISTRY);
    }

    private Tutor getOneTutor() {
        return new Tutor("0", "Lady Gaga", "Lady Gaga", "artpop@gmail.com", UserRepetinder.Subject.MUSIC);
    }

    private List<Tutor> getManyTutors(int n) {
        List<Tutor> tutors = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            tutors.add(new Tutor(Integer.toString(n), "Britney Spears #" + n, "Toxic", "oopsididitagain@gmail.com", UserRepetinder.Subject.BIOLOGY));
        }
        return tutors;
    }

    @Test
    public void testEmptyAlgorithm() {
        MatchingAlgorithm algo = new MatchingAlgorithm(new LinkedList<>());
        assertNull(algo.showNextUser(getOneStudent()));
    }

    @Test
    public void testShowOneTutor() {
        Tutor tutor = getOneTutor();
        MatchingAlgorithm algo = new MatchingAlgorithm(Collections.singletonList(tutor));
        assertEquals(tutor, algo.showNextUser(getOneStudent()));
    }

    @Test
    public void testSwipeRight() throws Exception {
        Tutor tutor = getOneTutor();
        Student student = getOneStudent();
        MatchingAlgorithm algo = new MatchingAlgorithm(Collections.singletonList(tutor));
        algo.swipeRight(student, tutor);
        assertEquals(1, tutor.getRang());
    }

    @Test
    public void testSwipeLeft() throws Exception {
        Tutor tutor = getOneTutor();
        Student student = getOneStudent();
        MatchingAlgorithm algo = new MatchingAlgorithm(Collections.singletonList(tutor));
        algo.swipeLeft(student, tutor);
        assertEquals(-1, tutor.getRang());
    }

    @Test
    public void testThreeTutors() throws Exception {
        List<Tutor> tutors = getManyTutors(3);
        Student student = getOneStudent();
        MatchingAlgorithm algo = new MatchingAlgorithm(tutors);

        Tutor fstTutor = algo.showNextUser(student);
        assertNotNull(fstTutor);
        algo.swipeRight(student, fstTutor);
        assertEquals(1, fstTutor.getRang());

        Tutor sndTutor = algo.showNextUser(student);
        assertNotNull(sndTutor);
        assertNotEquals(fstTutor, sndTutor);
        algo.swipeLeft(student, sndTutor);
        assertEquals(-1, sndTutor.getRang());

        Tutor thdTutor = algo.showNextUser(student);
        assertNotNull(thdTutor);
        assertNotEquals(fstTutor, thdTutor);
        assertNotEquals(sndTutor, thdTutor);
        algo.swipeRight(student, thdTutor);
        assertEquals(1, thdTutor.getRang());
    }

    @Test
    public void testThrowsMatchingException() throws Exception {
        Tutor tutor = getOneTutor();
        Student student = getOneStudent();
        MatchingAlgorithm algo = new MatchingAlgorithm(Collections.singletonList(tutor));
        algo.swipeRight(student, tutor);
        assertThrows(MatchingAlgorithmException.class, () -> algo.swipeRight(student, tutor));
    }


    // 1000 tutors for 1 s 324 ms, too long

    @Test
    public void testLotsOfTutors() throws MatchingAlgorithmException {
        int n = 1000;
        List<Tutor> tutors = getManyTutors(n);
        Student student = getOneStudent();
        Set<Tutor> swipedTutors = new HashSet<>();
        MatchingAlgorithm algo = new MatchingAlgorithm(tutors);
        for (int i = 0; i < n; i++) {
            Tutor tutor = algo.showNextUser(student);
            assertFalse(swipedTutors.contains(tutor));
            algo.swipeRight(student, tutor);
            swipedTutors.add(tutor);
        }
    }
}

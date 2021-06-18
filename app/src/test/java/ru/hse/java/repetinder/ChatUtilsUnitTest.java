package ru.hse.java.repetinder;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.hse.java.repetinder.chat.Chat;

import static org.junit.Assert.assertEquals;

public class ChatUtilsUnitTest {
    @Test
    public void testFormatter() {
        Map<String, Long> time = new HashMap<>();
        Chat chat = new Chat("hi", time, true);

        assertEquals("10", chat.returnLongWithZero(10));
        assertEquals("42", chat.returnLongWithZero(42));
        assertEquals("04", chat.returnLongWithZero(4));
        assertEquals("02", chat.returnLongWithZero(2));
    }

    @Test
    public void testDateLongAgo() {
        Map<String, Long> time = new HashMap<>();
        time.put("year", 2L);
        time.put("month", 2L);
        time.put("date", 2L);
        time.put("hours", 2L);
        time.put("minutes", 2L);
        Chat chat = new Chat("hi", time, true);

        assertEquals("02.02.1902, 02:02", chat.getTimeString());
    }

    @Test
    public void testDateToday() {
        Map<String, Long> time = new HashMap<>();
        Date today = Calendar.getInstance().getTime();
        time.put("year", (long) today.getYear());
        time.put("month", (long) today.getMonth());
        time.put("date", (long) today.getDate());
        time.put("hours", (long) today.getHours());
        time.put("minutes", (long) today.getMinutes());
        Chat chat = new Chat("hi", time, true);

        String expected = "today, " + chat.returnLongWithZero(today.getHours()) + ':' + today.getMinutes();
        assertEquals(expected, chat.getTimeString());
    }

    @Test
    public void testDateYesterday() {
        Map<String, Long> time = new HashMap<>();
        Date today = Calendar.getInstance().getTime();
        if (today.getDate() == 1) return;

        time.put("year", (long) today.getYear());
        time.put("month", (long) today.getMonth());
        time.put("date", (long) today.getDate() - 1);
        time.put("hours", (long) today.getHours());
        time.put("minutes", (long) today.getMinutes());
        Chat chat = new Chat("hi", time, true);

        String expected = "yesterday, " + chat.returnLongWithZero(today.getHours()) + ':' + today.getMinutes();
        assertEquals(expected, chat.getTimeString());
    }

    @Test
    public void testDateMyBirthday() {
        Map<String, Long> time = new HashMap<>();
        time.put("year", 102L);
        time.put("month", 3L);
        time.put("date", 7L);
        time.put("hours", 10L);
        time.put("minutes", 45L);
        Chat chat = new Chat("hi", time, true);

        assertEquals("07.03.2002, 10:45", chat.getTimeString());
    }

    @Test
    public void testDateRelative() {
        Map<String, Long> time = new HashMap<>();
        Date past = new Date(102, 3, 8);

        time.put("year", 102L);
        time.put("month", 3L);
        time.put("date", 7L);
        time.put("hours", 10L);
        time.put("minutes", 45L);
        Chat chat = new Chat("hi", time, true);

        assertEquals("yesterday, 10:45", chat.getTimeString(past));
    }
}

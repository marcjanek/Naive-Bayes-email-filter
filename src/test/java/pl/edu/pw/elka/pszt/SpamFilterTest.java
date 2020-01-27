package pl.edu.pw.elka.pszt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpamFilterTest {

    @Test
    void learn() {
        SpamFilter spamFilter = new SpamFilter();
        spamFilter.learn("A a AA a a a", true);
        spamFilter.learn("A a AA a a a", false);
        spamFilter.update();
        assertFalse(spamFilter.isSpam("A a AA a a a"));
    }

    @Test
    void update() {
        SpamFilter spamFilter = new SpamFilter();
        spamFilter.learn("A a AA a a a", true);
        spamFilter.update();
        assertTrue(spamFilter.isSpam("A a AA a a a"));
    }

    @Test
    void clear() {
        SpamFilter spamFilter = new SpamFilter();
        spamFilter.learn("A a AA a a a", true);
        spamFilter.update();
        spamFilter.clear();
        assertFalse(spamFilter.isSpam("A a AA a a a"));
    }
}
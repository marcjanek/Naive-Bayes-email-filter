package pl.edu.pw.elka.pszt.algorithm;

import org.junit.jupiter.api.Test;
import pl.edu.pw.elka.pszt.model.Word;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NaiveBayesTest {
    private static final Map<String, Word> map = new TreeMap<>();
    private static final NaiveBayes naiveBayes = new NaiveBayes(map);

    static {
        for (int i = 3; i < 100; ++i) {
            map.put(String.valueOf(i),
                    new Word(String.valueOf(i),
                            (i * 5) % 19,
                            (i * 19) % 17,
                            0,
                            0
                    )
            );
        }
        AtomicInteger totalHams = new AtomicInteger();
        AtomicInteger totalSpams = new AtomicInteger();
        map.forEach((k, v) -> {
            totalHams.addAndGet(v.getHam());
            totalSpams.addAndGet(v.getSpam());
        });
        map.forEach((k, v) -> {
            v.setProbabilityOfSpam(totalSpams.get() == 0 ? 1 : (double) v.getSpam() / (double) totalSpams.get());
            v.setProbabilityOfHam(totalHams.get() == 0 ? 1 : (double) v.getHam() / (double) totalHams.get());
        });
        naiveBayes.setTotalSpamEmails(totalSpams.get());
        naiveBayes.setTotalHamEmails(totalHams.get());
    }

    @Test
    void isHam() {
        assertNotNull(map);
        map.forEach((k, v) -> assertEquals(Math.log10(v.getProbabilityOfHam() / v.getProbabilityOfSpam()) + Math.log10(naiveBayes.getTotalHamEmails() / naiveBayes.getTotalSpamEmails()) > 0,
                naiveBayes.isHam(new String[]{v.getName()})));
    }

    @Test
    void isSpam() {
        assertNotNull(map);
        map.forEach((k, v) -> assertEquals(Math.log10(v.getProbabilityOfSpam() / v.getProbabilityOfHam()) + Math.log10(naiveBayes.getTotalSpamEmails() / naiveBayes.getTotalHamEmails()) > 0,
                naiveBayes.isSpam(new String[]{v.getName()})));
    }

    @Test
    void probabilityOfSpamWord() {
        assertNotNull(map);
        map.forEach((k, v) ->
                assertEquals((double) v.getSpam() / naiveBayes.getTotalSpamEmails(),
                        naiveBayes.probabilityOfSpamWord((int) naiveBayes.getTotalSpamEmails(), v))
        );
    }

    @Test
    void probabilityOfHamWord() {
        assertNotNull(map);
        map.forEach((k, v) ->
                assertEquals((double) v.getHam() / naiveBayes.getTotalHamEmails(),
                        naiveBayes.probabilityOfHamWord((int) naiveBayes.getTotalHamEmails(), v))
        );
    }
}
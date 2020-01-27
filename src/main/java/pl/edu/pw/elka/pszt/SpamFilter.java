package pl.edu.pw.elka.pszt;

import org.apache.commons.lang3.math.NumberUtils;
import pl.edu.pw.elka.pszt.algorithm.NaiveBayes;
import pl.edu.pw.elka.pszt.model.Word;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class SpamFilter {
    private final Map<String, Word> map;
    private final NaiveBayes naiveBayes;
    public String regex = " |\r\n|\r|\n";
    private int hams = 0;
    private int spams = 0;

    public SpamFilter() {
        this.map = new TreeMap<>();
        this.naiveBayes = new NaiveBayes(map);
    }

    public void learn(final String sentence, final boolean isSpam) {
        for (final String wordString : filter(sentence.split(regex))) {
            Word wordClass = new Word(wordString);

            if (!Objects.isNull(map.putIfAbsent(wordString, wordClass))) {
                wordClass = map.get(wordString);
            }
            if (isSpam) {
                wordClass.incrementSpam();
            } else {
                wordClass.incrementHam();
            }
        }
        if (isSpam) {
            ++spams;
        } else {
            ++hams;
        }
    }

    public void update() {
        naiveBayes.setTotalHamEmails(hams);
        naiveBayes.setTotalSpamEmails(spams);
        map.forEach((key, value) -> {
            value.setProbabilityOfHam(naiveBayes.probabilityOfHamWord(hams, value));
            value.setProbabilityOfSpam(naiveBayes.probabilityOfSpamWord(spams, value));
        });
    }

    public boolean isSpam(final String string) {
        return naiveBayes.isSpam(filter(string.split(regex)));
    }

    public boolean isHam(final String string) {
        return naiveBayes.isHam(filter(string.split(regex)));
    }

    public void clear() {
        map.clear();
    }

    private String[] filter(String[] strings) {
        return Arrays.stream(strings)
                .filter(e -> e.length() != 1 || Character.isLetter(e.charAt(0)))
                .filter(e -> {
                    if (e.length() == 1)
                        return true;
                    long count = e.chars().filter(c -> !Character.isLetter(c)).count();
                    if (count > 2)
                        return false;
                    if (count == 0)
                        return true;
                    return !(count == 1 && Character.isLetter(e.charAt(e.length() - 1)));
                })
                .map(String::toLowerCase)
                .map(e -> {
                    if (e.length() > 0 && !Character.isLetter(e.charAt(e.length() - 1)))
                        return e.substring(0, e.length() - 1);
                    return e;
                })
                .filter(e -> !NumberUtils.isNumber(e))
                .filter(e -> !"".equals(e))
                .toArray(String[]::new);
    }
}

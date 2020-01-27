package pl.edu.pw.elka.pszt.algorithm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.edu.pw.elka.pszt.model.Word;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public class NaiveBayes {
    private final Map<String, Word> map;
    @Setter
    @Getter
    private double totalSpamEmails = 0;
    @Setter
    @Getter
    private double totalHamEmails = 0;

    public boolean isHam(String[] words) {
        return calculateHamProbability(totalHamEmails / totalSpamEmails, words) > 0;
    }

    public boolean isSpam(String[] words) {
        return calculateSpamProbability(totalSpamEmails / totalHamEmails, words) > 0;
    }

    public double probabilityOfSpamWord(int spam, Word word) {
        return probability(word.getSpam(), spam);
    }

    public double probabilityOfHamWord(int ham, Word word) {
        return probability(word.getHam(), ham);
    }

    private double calculateHamProbability(double initValue, String[] words) {
        return Arrays.stream(words).mapToDouble(e -> {
            final Word wordClass = map.get(e);
            if (wordClass == null)
                return 0;
            return Math.log10(wordClass.getProbabilityOfHam() / wordClass.getProbabilityOfSpam());
        }).sum() + Math.log10(initValue);
    }

    private double calculateSpamProbability(double initValue, String[] words) {
        return Arrays.stream(words).mapToDouble(e -> {
            final Word wordClass = map.get(e);
            if (wordClass == null)
                return 0;
            return Math.log10(wordClass.getProbabilityOfSpam() / wordClass.getProbabilityOfHam());
        }).sum() + Math.log10(initValue);
    }

    private double probability(double nominator, double denominator) {
        return denominator == 0 ? 1 : nominator / denominator;
    }
}

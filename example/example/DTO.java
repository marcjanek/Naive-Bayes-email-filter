package example;

import lombok.Value;

@Value
public class DTO {
    private int hamsSuccess;
    private int hamsFails;
    private int spamsSuccess;
    private int spamsFails;
    private long averageTime;

    @Override
    public String toString() {
        return String.format("hamsSuccess: %d, hamTests: %d, success percentage: %d\n" +
                        "spamsSuccess: %d, spamTests: %d, success percentage: %d\n" +
                        "averageTime[ms]: %d\n",
                hamsSuccess,
                hamsSuccess + hamsFails,
                (int) ((double) hamsSuccess * 100 / (double) (hamsSuccess + hamsFails)),
                spamsSuccess,
                spamsSuccess + spamsFails,
                (int) ((double) spamsSuccess * 100 / (double) (spamsSuccess + spamsFails)),
                averageTime
        );
    }
}

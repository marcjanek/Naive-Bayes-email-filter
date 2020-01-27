import pl.edu.pw.elka.pszt.SpamFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Example extends DataLoader {
    private final static String DATA_PATH = "example/data/";

    public static void main(String[] args) throws IOException {
        Example example = new Example();
        final DTO dto = example.example(0.9);
        System.out.println(dto);
    }

    public DTO example(final double value) throws IOException {
        SpamFilter spamFilter = new SpamFilter();

        File file = new File(DATA_PATH);

        //load data
        List<String> hams = loadData(file, "/ham");
        List<String> spams = loadData(file, "/spam");

        //shuffle data
        Collections.shuffle(hams);
        Collections.shuffle(spams);
        //learn

        for (int i = 0; i < hams.size() * value; ++i) {
            spamFilter.learn(hams.get(i), false);
        }
        for (int i = 0; i < spams.size() * value; ++i) {
            spamFilter.learn(spams.get(i), true);
        }
        //calculate
        spamFilter.update();

        //test
        int hamsSuccess = 0;
        int hamsFails = 0;
        int spamsSuccess = 0;
        int spamsFails = 0;
        final long before = System.currentTimeMillis();
        for (int i = (int) (hams.size() * value); i < hams.size(); ++i) {
            if (spamFilter.isSpam(hams.get(i)))
                hamsFails++;
            else
                ++hamsSuccess;
        }
        int c = 0;
        for (int i = (int) (spams.size() * value); i < spams.size(); ++i, ++c) {
            if (!spamFilter.isSpam(spams.get(i)))
                spamsFails++;
            else
                ++spamsSuccess;
        }
        return new DTO(hamsSuccess,
                hamsFails,
                spamsSuccess,
                spamsFails,
                (System.currentTimeMillis() - before) / (hamsSuccess + hamsFails + spamsSuccess + spamsFails)
        );
    }
}


import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.LinkedList;
import java.util.List;

public class Chart {
    private static final int STEP = 10;
    private static final int ATTEMPTS = 2;

    public static void main(String[] args) throws Exception {

        List<Double> xValues = new LinkedList<>();
        List<Double> hamsSuccessPercentage = new LinkedList<>();
        List<Double> spamsSuccessPercentage = new LinkedList<>();
        final Example example = new Example();
        //get data
        for (double i = 0; i < 100; i += STEP) {
            System.out.println(i);
            xValues.add(i);
            double hamPercentage = 0;
            double spamPercentage = 0;
            for (int attempt = 0; attempt < ATTEMPTS; ++attempt) {
                final DTO dto = example.example(i / 100);
                hamPercentage += (double) dto.getHamsSuccess() * 100 / (double) (dto.getHamsSuccess() + dto.getHamsFails());
                spamPercentage += (double) dto.getSpamsSuccess() * 100 / (double) (dto.getSpamsSuccess() + dto.getSpamsFails());
            }
            hamsSuccessPercentage.add(hamPercentage / ATTEMPTS);
            spamsSuccessPercentage.add(spamPercentage / ATTEMPTS);
        }
        //create chart
        XYChart chart = new XYChartBuilder().width(800)
                .height(800)
                .title("Skuteczność filtra spamowego w zależności od ilości danych treningowych")
                .xAxisTitle("procent danych traningowych")
                .yAxisTitle("procent prawidłowych ocen maila")
                .build();
        //add series
        chart.addSeries("spam mails", xValues, spamsSuccessPercentage);
        chart.addSeries("ham mails", xValues, hamsSuccessPercentage);
        //save as png
        BitmapEncoder.saveBitmap(chart, "./chart", BitmapEncoder.BitmapFormat.PNG);
        //show
        new SwingWrapper<>(chart).displayChart();
    }
}
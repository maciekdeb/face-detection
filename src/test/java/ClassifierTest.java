import org.junit.Test;
import pl.lodz.p.ics.Utils;
import pl.lodz.p.ics.model.DataSample;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * User: maciek
 * Date: 15.12.13
 * Time: 22:14
 */
public class ClassifierTest {

    @Test
    public void testPasses() throws IOException {

        List<DataSample> dataSamplesFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/train/face/"), 1.0, 300, true);
        List<DataSample> dataTestFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/test/face/"), 1.0, true);

        List<DataSample> dataSamplesNonFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/train/non-face/"), 0.0, 500, true);
        List<DataSample> dataTestNonFaces = Utils.loadDataSet(new File("/home/maciek/baza-test/test/non-face/"), 0.0, true);

        System.out.println("Zbiór treningowy\nIlość próbek z twarzą: " + dataSamplesFaces.size() + "\nIlość próbek bez twarzy: " + dataSamplesNonFaces.size()
        +"\nZbiór testowy\nIlość próbek z twarzą: " + dataTestFaces.size() + "\nIlość próbek bez twarzy: " + dataTestNonFaces.size());


        int[] haarLikeFeatureNumbers = new int[]{1, 3, 4, 5, 7};
        List<Feature> features = new ArrayList<Feature>();
        System.out.println("\nLiczba cech:");
        for (int number : haarLikeFeatureNumbers) {
            List<Feature> featuresElement = Utils.loadFeatures(new File("/home/maciek/Dropbox/workspaces/java/face-detection/kopia features/feature" + number + "/xml/"));
            features.addAll(featuresElement);
            System.out.println(number + "-rodzaju: " + featuresElement.size());
        }


        StrongClassifier strongClassifier = new StrongClassifier(features, dataSamplesFaces, dataSamplesNonFaces, StrongClassifier.FIND_THRESHOLD);
//        StrongClassifier strongClassifier = Utils.loadClassifier("strongClassifier-100-1389215372691");
        int WEAK_CLASS_NUMBER = 150;
        strongClassifier.learn(WEAK_CLASS_NUMBER);

        System.out.print("\nWartosci wspolczynnikow klasyfikatora silnego: ");
        for (double d : strongClassifier.getAlfaFactor()) {
            System.out.print(d + " ");
        }
        System.out.println("\nKlasyfikatory slabe: " + Arrays.toString(strongClassifier.getWeakClassifiers()));

        List<DataSample> dataTest = new ArrayList<DataSample>();
        dataTest.addAll(dataTestFaces);
        dataTest.addAll(dataTestNonFaces);
        double ratio = 0;
        int testSize = 900;
        boolean used[] = new boolean[testSize];

        double falsePositive = 0;
        double truePositive = 0;
        double falseNegative = 0;
        double trueNegative = 0;

        for (int i = 1; i < testSize; i++) {

            int index;
            do {
                index = new Random().nextInt(testSize);
            } while (used[index] == true);

            DataSample ds = dataTest.get(index);
            double faceDetect = strongClassifier.detect(ds.getIntegralImage(), new Point(0, 0));

            if (faceDetect == ds.getY()) {
                if (faceDetect == 1.0) {
                    truePositive++;
                } else {
                    trueNegative++;
                }
                ratio++;
            } else {
                if (faceDetect == 1.0) {
                    falsePositive++;
                } else {
                    falseNegative++;
                }
            }

            System.out.print("\nPróbka nr: " + i + "\tWartość oczekiwana: " + ds.getY() + " wart. otrzymana: " + faceDetect);

            System.out.println("\n\tWspolczynnik poprawnych: " + ratio / i + "\ntruePositive: " + truePositive / i + "\ttrueNegative" + trueNegative / i + "\tfalsePositive: " + falsePositive / i + "\tfalseNegative: " + falseNegative / i);
        }


        Utils.saveClassifier(strongClassifier, "strongClassifier-" + WEAK_CLASS_NUMBER + "-");
        WeakClassifier[] weakClassifiers = strongClassifier.getWeakClassifiers();
        List<Feature> features1 = new ArrayList<Feature>();
        for (int i = 0; i < WEAK_CLASS_NUMBER; i++) {
            features1.add(weakClassifiers[i].getFeature());
        }
        Utils.drawFeature(features1,"feat-5-", 10);
    }



}

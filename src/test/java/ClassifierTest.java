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

    public static final int TRAIN_FACES = 500;
    public static final int TRAIN_NON_FACES = 100;

    public static final int TEST_FACES = 200;
    public static final int TEST_NON_FACES = 200;
    public static final int TEST_SIZE = TEST_FACES + TEST_NON_FACES;

    static final int[] haarLikeFeatureNumbers = new int[]{1, 3, 5, 7, 9};

    static final int WEAK_CLASS_NUMBER = 100;


    @Test
    public void testPasses() throws IOException {

        List<DataSample> dataSamplesFaces = Utils.loadDataSet(new File("/home/maciek/FACE/baza19/train/face/"), 1.0, TRAIN_FACES, true);
        List<DataSample> dataTestFaces = Utils.loadDataSet(new File("/home/maciek/FACE/baza19/test/face/"), 1.0, TEST_FACES, true);

        List<DataSample> dataSamplesNonFaces = Utils.loadDataSet(new File("/home/maciek/FACE/baza19/train/non-face/"), 0.0, TRAIN_NON_FACES, true);
        List<DataSample> dataTestNonFaces = Utils.loadDataSet(new File("/home/maciek/FACE/baza19/test/non-face/"), 0.0, TEST_NON_FACES, true);

        System.out.println("Zbiór treningowy\nIlość próbek z twarzą: " + dataSamplesFaces.size() + "\nIlość próbek bez twarzy: " + dataSamplesNonFaces.size()
        +"\nZbiór testowy\nIlość próbek z twarzą: " + dataTestFaces.size() + "\nIlość próbek bez twarzy: " + dataTestNonFaces.size());


        List<Feature> features = new ArrayList<Feature>();
        System.out.println("\nLiczba cech:");
        for (int number : haarLikeFeatureNumbers) {
            List<Feature> featuresElement = Utils.loadFeatures(new File("/home/maciek/FACE/features19/feature" + number + "/xml/"));
            features.addAll(featuresElement);
            System.out.println(number + "-rodzaju: " + featuresElement.size());
        }


        StrongClassifier strongClassifier = new StrongClassifier(features, dataSamplesFaces, dataSamplesNonFaces, StrongClassifier.USE_CONSTANT_THRESHOLD);
//        StrongClassifier strongClassifier = Utils.loadClassifier("strongClassifier-50-1389380955552");
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
        boolean used[] = new boolean[TEST_SIZE];

        double falsePositive = 0;
        double truePositive = 0;
        double falseNegative = 0;
        double trueNegative = 0;

        for (int i = 1; i < TEST_SIZE; i++) {

            int index;
            do {
                index = new Random().nextInt(TEST_SIZE);
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

            System.out.print("\nPróbka nr: " + i + "\tWartość oczekiwana: " + ds.getY() + " wart. otrzymana: " + faceDetect +
                "\n\tWspolczynnik poprawnych: " + ratio / i + "\ntruePositive: " + truePositive+ "\ttrueNegative" + trueNegative + "\tfalsePositive: " + falsePositive + "\tfalseNegative: " + falseNegative);
        }

        System.out.println(truePositive / TEST_FACES);

        Utils.saveClassifier(strongClassifier, "strongClassifier-" + WEAK_CLASS_NUMBER + "-");
        WeakClassifier[] weakClassifiers = strongClassifier.getWeakClassifiers();
        List<Feature> features1 = new ArrayList<Feature>();
        for (int i = 0; i < WEAK_CLASS_NUMBER; i++) {
            features1.add(weakClassifiers[i].getFeature());
        }
        Utils.drawFeature(features1,"feat-5-", 10);
    }



}

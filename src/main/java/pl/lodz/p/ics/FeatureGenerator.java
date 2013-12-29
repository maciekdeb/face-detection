package pl.lodz.p.ics;

import au.com.bytecode.opencsv.CSVReader;
import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.Feature;
import pl.lodz.p.ics.model.classification.Field;

import javax.xml.bind.*;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maciek
 * Date: 28.12.13
 * Time: 22:58
 */
public class FeatureGenerator {

    public static void main(String[] args) throws FileNotFoundException, JAXBException {

        saveFeature();

    }

    public static List<Feature> generateFeaturesCollection(List<Feature> featuresReference, int width, int height, int xSizing, int ySizing, int xMoving, int yMoving) {

        List<Feature> resizedFeatures = new ArrayList<Feature>();

        //Generowanie możliwych wielkości pol w suboknie cechy
        for (Feature referenceFeature : featuresReference) {

            List<Field> referencefields = referenceFeature.getFields();
            Point bottomPoint = referenceFeature.getFeatureMaxPoint();

            //TODO TEST
            for (int i = 0; i + bottomPoint.getX() < width; i += xSizing) {
                for (int j = 0; j + bottomPoint.getY() < height; j += ySizing) {

                    List<Field> fields = new ArrayList<Field>();
                    for (Field field : referencefields) {
                        fields.add(new Field(new Point(field.getTopLeftPoint().getX(), field.getTopLeftPoint().getY()), field.getBottomRightPoint().add(i, j), field.getWeight()));
                    }

                    Feature resizedFeature = new Feature(fields);
                    resizedFeatures.add(resizedFeature);
                }
            }

        }

        List<Feature> resultFeatures = new ArrayList<Feature>();

        // Generowanie przesunietych cech
        for (Feature feature : resizedFeatures) {

            List<Field> resizedFields = feature.getFields();
            Point bottomPoint = feature.getFeatureMaxPoint();

            for (int i = 0; i + bottomPoint.getX() < width - 1; i += xMoving) {
                for (int j = 0; j + bottomPoint.getY() < height - 1; j += yMoving) {

                    List<Field> fields = new ArrayList<Field>();
                    for (Field field : resizedFields) {
                        fields.add(new Field(field.getTopLeftPoint().add(i, j), field.getBottomRightPoint().add(i, j), field.getWeight()));                       ;
                    }

                    Feature movedFeature = new Feature(fields);
                    resultFeatures.add(movedFeature);
                }
            }

        }

        return resultFeatures;
    }

    public List<Feature> loadReferenceFeatures(List<String> fileNames) {

        List<Feature> features = new ArrayList<Feature>();

        try {

            JAXBContext context = JAXBContext.newInstance(Feature.class);

            for (String name : fileNames) {
                Unmarshaller um = context.createUnmarshaller();
                features.add((Feature) um.unmarshal(new File(name)));
            }

        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return features;
    }

    public static void saveFeature() {

        List<Field> fieldList = new ArrayList<Field>();
        fieldList.add(new Field(new Point(1, 1), new Point(2, 3), 1));
        fieldList.add(new Field(new Point(2, 1), new Point(3, 3), -1));
        Feature feature = new Feature(fieldList);

        JAXBContext context = null;
        try {

            context = JAXBContext.newInstance(Feature.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File("feature.xml");
            m.marshal(feature, file);

        } catch (PropertyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}

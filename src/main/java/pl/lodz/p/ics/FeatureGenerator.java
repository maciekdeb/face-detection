package pl.lodz.p.ics;

import pl.lodz.p.ics.model.Point;
import pl.lodz.p.ics.model.classification.Feature;
import pl.lodz.p.ics.model.classification.Field;

import javax.xml.bind.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maciek
 * Date: 28.12.13
 * Time: 22:58
 */
public class FeatureGenerator {

    public List<Feature> generateMovedFeatures(List<Feature> featureReference, int width, int height, int xMove, int yMove) {

        List<Feature> resizedFeatures = new ArrayList<Feature>();

        for (Feature feature : featureReference) {

            List<Field> resizedFields = feature.getFields();
            Point bottomPoint = feature.getFeatureMaxPoint();

            for (int i = 0; i + bottomPoint.getX() < width - 1; i += xMove) {

                for (int j = 0; j + bottomPoint.getY() < height - 1; j += yMove) {

                    List<Field> fields = new ArrayList<Field>();
                    for (Field field : resizedFields) {
                        Field movedField = new Field(field.getTopLeftPoint().add(i, j), field.getBottomRightPoint().add(i, j), field.getWeight());
                        movedField.setRowX(field.getRowX());
                        movedField.setColumnY(field.getColumnY());
                        fields.add(movedField);
                    }

                    Feature movedFeature = new Feature(fields);
                    movedFeature.setHeight(height);
                    movedFeature.setWidth(width);
                    resizedFeatures.add(movedFeature);
                }
            }

        }

        return resizedFeatures;

    }

    public List<Feature> generateResizedFeatures(List<Feature> featuresReference, int width, int height, int xSizing, int ySizing) {

        List<Feature> resizedFeatures = new ArrayList<Feature>();

        for (Feature referenceFeature : featuresReference) {

            List<Field> referenceFields = referenceFeature.getFields();
            Point farthest = referenceFeature.getFeatureMaxPoint();
            int xRow = referenceFeature.getMaxXRow();
            int yColumn = referenceFeature.getMaxYColumn();

            for (int i = 0; i * (xRow + 1) + farthest.getX() < width; i += xSizing) {

                for (int j = 0; j * (yColumn + 1) + farthest.getY() < height; j += ySizing) {

                    List<Field> fields = new ArrayList<Field>();

                    for (Field f : referenceFields) {

                        Point top = f.getTopLeftPoint();
                        Point bottom = f.getBottomRightPoint();

                        top = top.add(i * f.getRowX(), j * f.getColumnY());
                        bottom = bottom.add(i * (f.getRowX() + 1), j * (f.getColumnY() + 1));

                        Field resizedField = new Field(top, bottom, f.getWeight());
                        resizedField.setRowX(f.getRowX());
                        resizedField.setColumnY(f.getColumnY());
                        fields.add(resizedField);

                    }

                    Feature resizedFeature = new Feature(fields);
                    resizedFeature.setHeight(height);
                    resizedFeature.setWidth(width);
                    resizedFeatures.add(resizedFeature);
                }
            }

        }

        return resizedFeatures;
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

    public void saveFeature(List<Feature> features, String prefix) {

        int i = 0;
        for (Feature f : features) {

            JAXBContext context = null;
            try {

                context = JAXBContext.newInstance(Feature.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                File file = new File(prefix + "-" + (i++) + ".xml");
                m.marshal(f, file);

            } catch (PropertyException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (JAXBException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

    }

}

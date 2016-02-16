package edu.usm.it.controller;

import edu.usm.domain.BasicEntity;
import edu.usm.domain.Foundation;
import edu.usm.domain.Views;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by andrew on 2/15/16.
 */
public final class BayardTestUtilities {

    private static Map<String, List<Field>> jsonViewEntityFields;
    public static List<Field> foundationDetailsFields;
    public static List<Field> foundationListFields;

    /**
     * Validates that the field values for the provided BasicEntity exist in the response to a MockMvc GET request
     * to the specified URL. Ignores null values of the provided BasicEntity. Assumes that the property names of the
     * unmarshalled JSON match the field names of the entity exactly.
     *
     * @param jsonView The JSONView corresponding to the request
     * @param url Where to make the GET request
     * @param mockMvc The MockMvc dependency
     * @param entity The entity to validate
     * @throws Exception
     */
    public static void performEntityGetSingle(Class<? extends Views> jsonView, String url, MockMvc mockMvc, BasicEntity entity) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        List<Field> classFields = getEntityFields(jsonView);
        for(Field field: classFields) {
            field.setAccessible(true);
            if (field.get(entity) != null) {
                resultActions.andExpect(jsonPath("$."+field.getName(), is(field.get(entity))));
            }
        }

    }

    /**
     * Validates that the field values for the provided BasicEntities exist in the response to a MockMvc GET request
     * to the specified URL. This validation will ignore the ordering of entities in the response. Ignores null values
     * of the provided BasicEntities. Assumes that the property names of the unmarshalled JSON match the field names of
     * the entity exactly.
     *
     * @param jsonView The JSONView corresponding to the request
     * @param url Where to make the GET request
     * @param mockMvc The MockMvc dependency
     * @param entities The entities to validate
     * @throws Exception
     */
    public static void performEntityGetMultiple(Class<? extends Views> jsonView, String url, MockMvc mockMvc, BasicEntity... entities) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        List<Field> classFields = getEntityFields(jsonView);
        for (Field field: classFields) {
            field.setAccessible(true);
            List<Object> existingValues = new ArrayList<>();
            for (BasicEntity entity: entities) {
                if (null != field.get(entity)) {
                    existingValues.add(field.get(entity));
                }
            }
            resultActions.andExpect(jsonPath("$.[*]."+field.getName(), containsInAnyOrder(existingValues.toArray())));
        }

    }

    private static List<Field> getEntityFields(Class<? extends Views> jsonView) throws Exception {
        List<Field> entityFields = jsonViewEntityFields.get(jsonView.getSimpleName());
        if (null == entityFields) {
            throw new Exception("Entity fields do not exist for JSON Views: "+jsonView.getSimpleName()+". Fields must be initialized" +
                    " and registered in BayardTestUtilities.");
        }
        return entityFields;
    }

    /**
     * Initializes collections of fields for various BasicEntity subclasses, in detail- and list-specific forms.
     * Should mirror the conventions of our use of JsonView in the Rest API
     */
    static {
        try {
            jsonViewEntityFields = new HashMap<>();

            foundationListFields = new ArrayList<>();
            foundationListFields.add(Foundation.class.getDeclaredField("name"));
            foundationListFields.add(Foundation.class.getDeclaredField("address"));
            foundationListFields.add(Foundation.class.getDeclaredField("phoneNumber"));
            foundationListFields.add(Foundation.class.getDeclaredField("primaryContactName"));
            foundationListFields.add(Foundation.class.getDeclaredField("currentGrantor"));
            jsonViewEntityFields.put(Views.FoundationList.class.getSimpleName(), foundationListFields);

            foundationDetailsFields = new ArrayList<>(foundationListFields);
            foundationDetailsFields.add(Foundation.class.getDeclaredField("website"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("primaryContactTitle"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("primaryContactPhone"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("primaryContactEmail"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactName"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactTitle"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactPhone"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactEmail"));
            jsonViewEntityFields.put(Views.FoundationDetails.class.getSimpleName(), foundationDetailsFields);

        } catch (NoSuchFieldException e) {

        }

    }

}

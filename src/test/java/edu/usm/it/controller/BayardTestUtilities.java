package edu.usm.it.controller;

import edu.usm.domain.BasicEntity;
import edu.usm.domain.Foundation;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by andrew on 2/15/16.
 */
public class BayardTestUtilities {

    public static List<Field> foundationDetailsFields;
    public static List<Field> foundationListFields;

    /**
     * Validates that the field values for the provided BasicEntity exist in the response to a MockMvc GET request
     * to the specified URL. Ignores null values of the provided BasicEntity.
     *
     * @param classFields The fields of the BasicEntity to validate
     * @param url Where to make the GET request
     * @param entity The entity to validate
     * @param mockMvc The MockMvc dependency
     * @throws Exception
     */
    public static void performEntityGetSingle(List<Field> classFields, String url, MockMvc mockMvc, BasicEntity entity) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

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
     * of the provided BasicEntities.
     *
     * @param classFields The fields of the BasicEntity to validate
     * @param url Where to make the GET request
     * @param mockMvc
     * @param entities
     * @throws Exception
     */
    public static void performEntityGetMultiple(List<Field> classFields, String url, MockMvc mockMvc, BasicEntity... entities) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        for (Field field: classFields) {
            List<Object> existingValues = new ArrayList<>();
            for (BasicEntity entity: entities) {
                if (null != field.get(entity)) {
                    existingValues.add(field.get(entity));
                }
            }
            resultActions.andExpect(jsonPath("$.[*]."+field.getName(), containsInAnyOrder(existingValues.toArray())));
        }

    }

    /**
     * Initializes collections of fields for various BasicEntity subclasses, in detail- and list-specific forms.
     * Should mirror the conventions of our use of JsonView in the Rest API
     */
    static {
        try {
            foundationListFields = new ArrayList<>();
            foundationListFields.add(Foundation.class.getDeclaredField("name"));
            foundationListFields.add(Foundation.class.getDeclaredField("address"));
            foundationListFields.add(Foundation.class.getDeclaredField("phoneNumber"));
            foundationListFields.add(Foundation.class.getDeclaredField("primaryContactName"));
            foundationListFields.add(Foundation.class.getDeclaredField("currentGrantor"));
            foundationDetailsFields = new ArrayList<>(foundationListFields);
            foundationDetailsFields.add(Foundation.class.getDeclaredField("website"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("primaryContactTitle"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("primaryContactPhone"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("primaryContactEmail"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactName"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactTitle"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactPhone"));
            foundationDetailsFields.add(Foundation.class.getDeclaredField("secondaryContactEmail"));
        } catch (NoSuchFieldException e) {

        }

    }

}

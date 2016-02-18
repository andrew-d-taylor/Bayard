package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import edu.usm.domain.BasicEntity;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.Views;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andrew on 2/15/16.
 */
public final class BayardTestUtilities {

    private static Map<String, List<Field>> jsonViewEntityFields;
    public static List<Field> foundationDetailsFields;
    public static List<Field> foundationListFields;
    public static List<Field> grantDetailsFields;
    public static List<Field> grantListFields;

    private static ObjectMapper objectMapper;

    public static void performEntityDelete(String url, MockMvc mockMvc) throws Exception {
        mockMvc.perform(delete(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static void performEntityPost(String url, BasicEntity entity, MockMvc mockMvc) throws Exception {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isCreated());
    }

    public static void performEntityPut(String url, BasicEntity entity, MockMvc mockMvc) throws Exception {
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk());
    }

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
                Object fieldValue = field.get(entity);
                if (field.getType() == LocalDate.class) {
                    fieldValue = fieldValue.toString();
                }
                resultActions.andExpect(jsonPath("$."+field.getName(), is(fieldValue)));
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
                    Object fieldValue = field.get(entity);
                    //Bayard serializes LocalDates to its simple String representation
                    if (field.getType() == LocalDate.class) {
                        fieldValue = fieldValue.toString();
                    }
                    existingValues.add(fieldValue);
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
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JSR310Module());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

            grantListFields = new ArrayList<>();
            grantListFields.add(Grant.class.getDeclaredField("name"));
            grantListFields.add(Grant.class.getDeclaredField("startPeriod"));
            grantListFields.add(Grant.class.getDeclaredField("endPeriod"));
            grantListFields.add(Grant.class.getDeclaredField("intentDeadline"));
            grantListFields.add(Grant.class.getDeclaredField("applicationDeadline"));
            grantListFields.add(Grant.class.getDeclaredField("reportDeadline"));
            grantListFields.add(Grant.class.getDeclaredField("amountAppliedFor"));
            grantListFields.add(Grant.class.getDeclaredField("amountReceived"));
            jsonViewEntityFields.put(Views.GrantList.class.getSimpleName(), grantListFields);

            grantDetailsFields = new ArrayList<>(grantListFields);
            grantDetailsFields.add(Grant.class.getDeclaredField("description"));
            grantDetailsFields.add(Grant.class.getDeclaredField("restriction"));
            jsonViewEntityFields.put(Views.GrantDetails.class.getSimpleName(), grantDetailsFields);

        } catch (NoSuchFieldException e) {

        }

    }

}

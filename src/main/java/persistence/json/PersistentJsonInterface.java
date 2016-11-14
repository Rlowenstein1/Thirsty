package main.java.persistence.json;

import main.java.persistence.PersistenceInterface;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import org.hildan.fxgson.FxGson;

import java.time.LocalDateTime;

/**
 * Dominic Pattison
 */
abstract class PersistentJsonInterface implements PersistenceInterface {

    private final Gson gson;

    /**
     * Default constructor. All sub-classes automatically call this when they are themselves created
     */
    PersistentJsonInterface() {
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gson = gsonBuilder.create();
        gson = FxGson.coreBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    /**
     * Function for converting object to json string
     * @param o object to convert
     * @param c class of object
     * @return string of json representing the object o
     */
    String toJson(Object o, Type c) {
        return (gson.toJson(o, c));
    }

    /**
     * Generic function for converting a json string to an object
     * @param <T> type of the object to be created from the given JSON string
     * @param j string of json to convert
     * @param c class of type T of the json string object
     * @return object of type T parsed from the json string j
     */
    <T> T fromJson(String j, Class<T> c) {
        return (gson.fromJson(j, c));
    } 
}

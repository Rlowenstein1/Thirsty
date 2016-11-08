package persistence.json;

import persistence.PersistenceInterface;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import org.hildan.fxgson.FxGson;

import java.time.LocalDateTime;

public abstract class PersistentJSONInterface implements PersistenceInterface {

    private Gson gson;

    /**
     * Default constructor. All sub-classes automatically call this when they are themselves created
     */
    public PersistentJSONInterface() {
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
    public String toJSON(Object o, Type c) {
        return (gson.toJson(o, c));
    }

    /**
     * Generic function for converting a json string to an object
     * @param T type for the generic function
     * @param j string of json to convert
     * @param c class of the json string object
     * @return object of type T parsed from the json string j
     */
    public <T> T fromJSON(String j, Class<T> c) {
        return (gson.fromJson(j, c));
    } 
}

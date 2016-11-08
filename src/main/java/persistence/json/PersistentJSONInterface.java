/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.json;

import persistence.PersistenceInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import org.hildan.fxgson.FxGson;

/**
 *
 * @author tybrown
 */
public abstract class PersistentJSONInterface implements PersistenceInterface {

    private Gson gson;

    /**
     * Default constructor. All sub-classes automatically call this when they are themselves created
     */
    public PersistentJSONInterface() {
        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gson = gsonBuilder.create();
        gson = FxGson.coreBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public String toJSON(Object o, Type c) {
        return (gson.toJson(o, c));
    }

    public <T> T fromJSON(String j, Class<T> c) {
        return (gson.fromJson(j, c));
    } 
}

package db;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class SuperClassExclusionStrategy implements ExclusionStrategy {
    private final Class<?> excludedClass;

    public SuperClassExclusionStrategy(Class<?> excludedClass) {
        this.excludedClass = excludedClass;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return excludedClass.equals(clazz);
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return excludedClass.equals(f.getDeclaredClass());
    }
}

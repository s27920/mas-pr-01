package Models.Util;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class SuperObject implements Serializable {
    private static Map<Class, List> superObjectContainer = new HashMap<>();
    private static Map<Class, Map<String, Object>> staticValues = new HashMap<>();
    public static final String SUPER_OBJ_FILE = "objs.jso";

    public SuperObject() {
        addObj();
    }

    @SuppressWarnings("unchecked")
    private void addObj(){
        List list = superObjectContainer.computeIfAbsent(this.getClass(), l -> new ArrayList<>());
        list.add(this);
    }

    private static void writeStaticFields(ObjectOutputStream oos) throws IOException {
        superObjectContainer.forEach((k,v)-> {
            Map<String, Object> classStatics = staticValues.computeIfAbsent(k, key -> new HashMap<>());
            Arrays.stream(k.getDeclaredFields()).forEach((field -> {
                if (Modifier.isStatic(field.getModifiers())) {
                    try {
                        field.setAccessible(true);
                        classStatics.put(field.getName(), field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }));
        });
        oos.writeObject(staticValues);
    }

    @SuppressWarnings("unchecked")
    private static void readStaticFields(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        staticValues = (Map<Class, Map<String, Object>>) ois.readObject();
    }

    @SuppressWarnings("unchecked")
    private static void populateStaticFields(){
        staticValues.forEach((k,v) -> v.forEach((fieldName, fieldValue)->{
            try {
                Field field = k.getDeclaredField(fieldName);
                field.setAccessible(true);
                String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);                try {
                    Method setField = k.getDeclaredMethod(setterName, field.getType());
                    setField.invoke(null, fieldValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    field.set(null, fieldValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }));
    }

    public void removeObj(){
        superObjectContainer.computeIfPresent(this.getClass(), (c, list) -> {
            list.remove(this);
            return list;
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getObjectsFromClass(Class<T> c){
        return Collections.unmodifiableList(superObjectContainer.getOrDefault(c, new ArrayList<T>()));
    }

    public static void writeObjects() throws IOException{
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SUPER_OBJ_FILE))){
            oos.writeObject(superObjectContainer);
            writeStaticFields(oos);
        }
    }

    @SuppressWarnings("unchecked")
    public static void readObjects() throws IOException, ClassCastException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SUPER_OBJ_FILE))){
            superObjectContainer = (Map<Class, List>) ois.readObject();
            readStaticFields(ois);
            populateStaticFields();
        }
    }
}

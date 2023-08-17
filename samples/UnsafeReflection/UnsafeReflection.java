import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class LeetSpeak {
    private String changed;
    
    public LeetSpeak(String s) {
        changed = s
            .replace('a', '4').replace('e', '3').replace('i', '1')
            .replace('o', '0');
    }
    
    public String toString() {
        return changed;
    }
}


public class UnsafeReflection {
    public static void main(String[] args) {
        try {
            Class theClass = Class.forName(args[0]);
            Constructor theConstructor = theClass.getConstructor(String.class);
            System.out.println(theConstructor.newInstance(args[1]));
        } 
        catch (
            ClassNotFoundException | 
            InstantiationException | 
            IllegalAccessException | 
            NoSuchMethodException  | 
            InvocationTargetException ex) {
                System.err.println("Exception: " + ex);
            }
    }
    
}

package bloody.hell.kpractice.things.jni;

/**
 * Created by Mamert on 16.01.2017.
 */
public interface AJniCallbackReceiver {
    public abstract void simpleJniCallback(String s);
    public abstract String anotherJniCallback(String s);
}

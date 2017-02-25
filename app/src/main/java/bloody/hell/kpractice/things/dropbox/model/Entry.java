package bloody.hell.kpractice.things.dropbox.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mamert on 23.02.2017.
 */

public class Entry {
    public static final String ROOT_FOLDER_PATH = ""; // all others start with "/"

    public static enum Type{
        FILE("file"), FOLDER("folder"), WTF("wtf");

        private String name;
        Type(String name){
            this.name = name;
        }
        public static Type fromName(String name){
            for (Type t : Type.values()) {
                if (t.name.equalsIgnoreCase(name)) {
                    return t;
                }
            }
            return WTF;
        }
    }

    @SerializedName(".tag")
    public String _tag;
    public String name;
    public String id;
    public String server_modified;
    public String size;
    public String path_lower;
    public String path_display;
    public String content_hash;
    // there are others, too.





    public Type getType(){
        return Type.fromName(_tag);
    }


    // https://www.dropbox.com/developers/documentation/http/documentation#files-get_metadata
}

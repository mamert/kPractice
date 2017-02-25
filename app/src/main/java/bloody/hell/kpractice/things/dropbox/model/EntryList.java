package bloody.hell.kpractice.things.dropbox.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mamert on 23.02.2017.
 */

public class EntryList {

    public List<Entry> entries;
    public String cursor;
    public boolean has_more; // can use list_folder/continue

}

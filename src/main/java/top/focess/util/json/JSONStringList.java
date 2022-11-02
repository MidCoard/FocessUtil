package top.focess.util.json;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class JSONStringList extends AJSONList<String> {


    public JSONStringList(JSONList list) {
        super(list);
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return this.list.getValues().stream().map(Object::toString).iterator();
    }
}

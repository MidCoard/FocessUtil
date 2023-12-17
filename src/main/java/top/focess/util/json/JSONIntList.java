package top.focess.util.json;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class JSONIntList extends AJSONList<Integer> {


    public JSONIntList(JSONList list) {
        super(list);
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return this.list.getValues().stream().map(Object::toString).map(Integer::parseInt).iterator();
    }

}

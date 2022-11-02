package top.focess.util.json;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class JSONDoubleList extends AJSONList<Double> {


    public JSONDoubleList(JSONList list) {
        super(list);
    }

    @NotNull
    @Override
    public Iterator<Double> iterator() {
        return this.list.getValues().stream().map(Object::toString).map(Double::parseDouble).iterator();
    }
}

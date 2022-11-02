package top.focess.util.json;

import java.util.List;

public abstract class AJSONList<E> extends JSONObject implements IJSONList, Iterable<E> {

    protected final JSONList list;

    public AJSONList(JSONList list) {
        this.list = list;
    }

    @Override
    public String toJson() {
        return this.list.toJson();
    }

    @Override
    public JSONList getJSONList() {
        return this.list;
    }

    @Override
    public int size() {
        return this.list.size();
    }

    public E get(final int index) {
        return (E) this.list.getValues().get(index);
    }

    public List<E> getValues() {
        return (List<E>) this.list.getValues();
    }
}

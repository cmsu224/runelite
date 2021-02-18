//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.org.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JSONArray implements Iterable<Object> {
    private final ArrayList<Object> myArrayList;

    public JSONArray() {
        this.myArrayList = new ArrayList();
    }

    public JSONArray(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        } else if (x.nextClean() != ']') {
            x.back();

            while(true) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.myArrayList.add(JSONObject.NULL);
                } else {
                    x.back();
                    this.myArrayList.add(x.nextValue());
                }

                switch(x.nextClean()) {
                    case ',':
                        if (x.nextClean() == ']') {
                            return;
                        }

                        x.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }

    public JSONArray(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONArray(Collection<Object> collection) {
        this.myArrayList = new ArrayList();
        if (collection != null) {
            Iterator iter = collection.iterator();

            while(iter.hasNext()) {
                this.myArrayList.add(JSONObject.wrap(iter.next()));
            }
        }

    }

    public JSONArray(Object array) throws JSONException {
        this();
        if (!array.getClass().isArray()) {
            throw new JSONException("JSONArray initial value should be a string or collection or array.");
        } else {
            int length = Array.getLength(array);

            for(int i = 0; i < length; ++i) {
                this.put(JSONObject.wrap(Array.get(array, i)));
            }

        }
    }

    public Iterator<Object> iterator() {
        return this.myArrayList.iterator();
    }

    public Object get(int index) throws JSONException {
        Object object = this.opt(index);
        if (object == null) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        } else {
            return object;
        }
    }

    public boolean getBoolean(int index) throws JSONException {
        Object object = this.get(index);
        if (!object.equals(Boolean.FALSE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("false"))) {
            if (!object.equals(Boolean.TRUE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("true"))) {
                throw new JSONException("JSONArray[" + index + "] is not a boolean.");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public double getDouble(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        } catch (Exception var4) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public int getInt(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
        } catch (Exception var4) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public JSONArray getJSONArray(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        } else {
            throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
        }
    }

    public JSONObject getJSONObject(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        } else {
            throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
        }
    }

    public long getLong(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
        } catch (Exception var4) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public String getString(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        } else {
            throw new JSONException("JSONArray[" + index + "] not a string.");
        }
    }

    public boolean isNull(int index) {
        return JSONObject.NULL.equals(this.opt(index));
    }

    public String join(String separator) throws JSONException {
        int len = this.length();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(separator);
            }

            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }

        return sb.toString();
    }

    public int length() {
        return this.myArrayList.size();
    }

    public Object opt(int index) {
        return index >= 0 && index < this.length() ? this.myArrayList.get(index) : null;
    }

    public boolean optBoolean(int index) {
        return this.optBoolean(index, false);
    }

    public boolean optBoolean(int index, boolean defaultValue) {
        try {
            return this.getBoolean(index);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public double optDouble(int index) {
        return this.optDouble(index, 0.0D / 0.0);
    }

    public double optDouble(int index, double defaultValue) {
        try {
            return this.getDouble(index);
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public int optInt(int index) {
        return this.optInt(index, 0);
    }

    public int optInt(int index, int defaultValue) {
        try {
            return this.getInt(index);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public JSONArray optJSONArray(int index) {
        Object o = this.opt(index);
        return o instanceof JSONArray ? (JSONArray)o : null;
    }

    public JSONObject optJSONObject(int index) {
        Object o = this.opt(index);
        return o instanceof JSONObject ? (JSONObject)o : null;
    }

    public long optLong(int index) {
        return this.optLong(index, 0L);
    }

    public long optLong(int index, long defaultValue) {
        try {
            return this.getLong(index);
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public String optString(int index) {
        return this.optString(index, "");
    }

    public String optString(int index, String defaultValue) {
        Object object = this.opt(index);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }

    public JSONArray put(boolean value) {
        this.put((Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public JSONArray put(Collection<Object> value) {
        this.put((Object)(new JSONArray(value)));
        return this;
    }

    public JSONArray put(double value) throws JSONException {
        Double d = new Double(value);
        JSONObject.testValidity(d);
        this.put((Object)d);
        return this;
    }

    public JSONArray put(int value) {
        this.put((Object)(new Integer(value)));
        return this;
    }

    public JSONArray put(long value) {
        this.put((Object)(new Long(value)));
        return this;
    }

    public JSONArray put(Map<String, Object> value) {
        this.put((Object)(new JSONObject(value)));
        return this;
    }

    public JSONArray put(Object value) {
        this.myArrayList.add(value);
        return this;
    }

    public JSONArray put(int index, boolean value) throws JSONException {
        this.put(index, (Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public JSONArray put(int index, Collection<Object> value) throws JSONException {
        this.put(index, (Object)(new JSONArray(value)));
        return this;
    }

    public JSONArray put(int index, double value) throws JSONException {
        this.put(index, (Object)(new Double(value)));
        return this;
    }

    public JSONArray put(int index, int value) throws JSONException {
        this.put(index, (Object)(new Integer(value)));
        return this;
    }

    public JSONArray put(int index, long value) throws JSONException {
        this.put(index, (Object)(new Long(value)));
        return this;
    }

    public JSONArray put(int index, Map<String, Object> value) throws JSONException {
        this.put(index, (Object)(new JSONObject(value)));
        return this;
    }

    public JSONArray put(int index, Object value) throws JSONException {
        JSONObject.testValidity(value);
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        } else {
            if (index < this.length()) {
                this.myArrayList.set(index, value);
            } else {
                while(index != this.length()) {
                    this.put(JSONObject.NULL);
                }

                this.put(value);
            }

            return this;
        }
    }

    public Object remove(int index) {
        return index >= 0 && index < this.length() ? this.myArrayList.remove(index) : null;
    }

    public boolean similar(Object other) {
        if (!(other instanceof JSONArray)) {
            return false;
        } else {
            int len = this.length();
            if (len != ((JSONArray)other).length()) {
                return false;
            } else {
                for(int i = 0; i < len; ++i) {
                    Object valueThis = this.get(i);
                    Object valueOther = ((JSONArray)other).get(i);
                    if (valueThis instanceof JSONObject) {
                        if (!((JSONObject)valueThis).similar(valueOther)) {
                            return false;
                        }
                    } else if (valueThis instanceof JSONArray) {
                        if (!((JSONArray)valueThis).similar(valueOther)) {
                            return false;
                        }
                    } else if (!valueThis.equals(valueOther)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public JSONObject toJSONObject(JSONArray names) throws JSONException {
        if (names != null && names.length() != 0 && this.length() != 0) {
            JSONObject jo = new JSONObject();

            for(int i = 0; i < names.length(); ++i) {
                jo.put(names.getString(i), this.opt(i));
            }

            return jo;
        } else {
            return null;
        }
    }

    public String toString() {
        try {
            return this.toString(0);
        } catch (Exception var2) {
            return null;
        }
    }

    public String toString(int indentFactor) throws JSONException {
        StringWriter sw = new StringWriter();
        synchronized(sw.getBuffer()) {
            return this.write(sw, indentFactor, 0).toString();
        }
    }

    public Writer write(Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }

    Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            boolean commanate = false;
            int length = this.length();
            writer.write(91);
            if (length == 1) {
                JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
            } else if (length != 0) {
                int newindent = indent + indentFactor;

                for(int i = 0; i < length; ++i) {
                    if (commanate) {
                        writer.write(44);
                    }

                    if (indentFactor > 0) {
                        writer.write(10);
                    }

                    JSONObject.indent(writer, newindent);
                    JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent);
                    commanate = true;
                }

                if (indentFactor > 0) {
                    writer.write(10);
                }

                JSONObject.indent(writer, indent);
            }

            writer.write(93);
            return writer;
        } catch (IOException var8) {
            throw new JSONException(var8);
        }
    }
}

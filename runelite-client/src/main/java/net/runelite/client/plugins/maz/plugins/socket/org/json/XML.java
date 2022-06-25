/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.maz.plugins.socket.org.json;

import java.util.Iterator;

public class XML {
    public static final Character AMP = Character.valueOf('&');
    public static final Character APOS = Character.valueOf('\'');
    public static final Character BANG = Character.valueOf('!');
    public static final Character EQ = Character.valueOf('=');
    public static final Character GT = Character.valueOf('>');
    public static final Character LT = Character.valueOf('<');
    public static final Character QUEST = Character.valueOf('?');
    public static final Character QUOT = Character.valueOf('\"');
    public static final Character SLASH = Character.valueOf('/');

    public static String escape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        int length = string.length();
        block7: for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            switch (c) {
                case '&': {
                    sb.append("&amp;");
                    continue block7;
                }
                case '<': {
                    sb.append("&lt;");
                    continue block7;
                }
                case '>': {
                    sb.append("&gt;");
                    continue block7;
                }
                case '\"': {
                    sb.append("&quot;");
                    continue block7;
                }
                case '\'': {
                    sb.append("&apos;");
                    continue block7;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static void noSpace(String string) throws JSONException {
        int length = string.length();
        if (length == 0) {
            throw new JSONException("Empty string.");
        }
        for (int i = 0; i < length; ++i) {
            if (!Character.isWhitespace(string.charAt(i))) continue;
            throw new JSONException("'" + string + "' contains a space character.");
        }
    }

    private static boolean parse(XMLTokener x, JSONObject context, String name) throws JSONException {
        String string;
        JSONObject jsonobject = null;
        Object token = x.nextToken();
        if (token == BANG) {
            char c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                token = x.nextToken();
                if ("CDATA".equals(token) && x.next() == '[') {
                    String string2 = x.nextCDATA();
                    if (string2.length() > 0) {
                        context.accumulate("content", string2);
                    }
                    return false;
                }
                throw x.syntaxError("Expected 'CDATA['");
            }
            int i = 1;
            do {
                if ((token = x.nextMeta()) == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                }
                if (token == LT) {
                    ++i;
                    continue;
                }
                if (token != GT) continue;
                --i;
            } while (i > 0);
            return false;
        }
        if (token == QUEST) {
            x.skipPast("?>");
            return false;
        }
        if (token == SLASH) {
            token = x.nextToken();
            if (name == null) {
                throw x.syntaxError("Mismatched close tag " + token);
            }
            if (!token.equals(name)) {
                throw x.syntaxError("Mismatched " + name + " and " + token);
            }
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped close tag");
            }
            return true;
        }
        if (token instanceof Character) {
            throw x.syntaxError("Misshaped tag");
        }
        String tagName = (String)token;
        token = null;
        jsonobject = new JSONObject();
        while (true) {
            if (token == null) {
                token = x.nextToken();
            }
            if (!(token instanceof String)) break;
            string = (String)token;
            token = x.nextToken();
            if (token == EQ) {
                token = x.nextToken();
                if (!(token instanceof String)) {
                    throw x.syntaxError("Missing value");
                }
                jsonobject.accumulate(string, XML.stringToValue((String)token));
                token = null;
                continue;
            }
            jsonobject.accumulate(string, "");
        }
        if (token == SLASH) {
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped tag");
            }
            if (jsonobject.length() > 0) {
                context.accumulate(tagName, jsonobject);
            } else {
                context.accumulate(tagName, "");
            }
            return false;
        }
        if (token == GT) {
            while (true) {
                if ((token = x.nextContent()) == null) {
                    if (tagName != null) {
                        throw x.syntaxError("Unclosed tag " + tagName);
                    }
                    return false;
                }
                if (token instanceof String) {
                    string = (String)token;
                    if (string.length() <= 0) continue;
                    jsonobject.accumulate("content", XML.stringToValue(string));
                    continue;
                }
                if (token == LT && XML.parse(x, jsonobject, tagName)) break;
            }
            if (jsonobject.length() == 0) {
                context.accumulate(tagName, "");
            } else if (jsonobject.length() == 1 && jsonobject.opt("content") != null) {
                context.accumulate(tagName, jsonobject.opt("content"));
            } else {
                context.accumulate(tagName, jsonobject);
            }
            return false;
        }
        throw x.syntaxError("Misshaped tag");
    }

    public static Object stringToValue(String string) {
        if ("true".equalsIgnoreCase(string)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(string)) {
            return Boolean.FALSE;
        }
        if ("null".equalsIgnoreCase(string)) {
            return JSONObject.NULL;
        }
        try {
            Long value;
            char initial = string.charAt(0);
            if ((initial == '-' || initial >= '0' && initial <= '9') && (value = new Long(string)).toString().equals(string)) {
                return value;
            }
        }
        catch (Exception ignore) {
            try {
                Double value = new Double(string);
                if (value.toString().equals(string)) {
                    return value;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return string;
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        XMLTokener x = new XMLTokener(string);
        while (x.more() && x.skipPast("<")) {
            XML.parse(x, jo, null);
        }
        return jo;
    }

    public static String toString(Object object) throws JSONException {
        return XML.toString(object, null);
    }

    public static String toString(Object object, String tagName) throws JSONException {
        String string;
        StringBuilder sb = new StringBuilder();
        if (object instanceof JSONObject) {
            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }
            JSONObject jo = (JSONObject)object;
            Iterator<String> keys = jo.keys();
            while (keys.hasNext()) {
                int i;
                int length;
                JSONArray ja;
                String string2;
                String key = keys.next();
                Object value = jo.opt(key);
                if (value == null) {
                    value = "";
                }
                String string3 = string2 = value instanceof String ? (String)value : null;
                if ("content".equals(key)) {
                    if (value instanceof JSONArray) {
                        ja = (JSONArray)value;
                        length = ja.length();
                        for (i = 0; i < length; ++i) {
                            if (i > 0) {
                                sb.append('\n');
                            }
                            sb.append(XML.escape(ja.get(i).toString()));
                        }
                        continue;
                    }
                    sb.append(XML.escape(value.toString()));
                    continue;
                }
                if (value instanceof JSONArray) {
                    ja = (JSONArray)value;
                    length = ja.length();
                    for (i = 0; i < length; ++i) {
                        value = ja.get(i);
                        if (value instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(XML.toString(value));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                            continue;
                        }
                        sb.append(XML.toString(value, key));
                    }
                    continue;
                }
                if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");
                    continue;
                }
                sb.append(XML.toString(value, key));
            }
            if (tagName != null) {
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();
        }
        if (object.getClass().isArray()) {
            object = new JSONArray(object);
        }
        if (object instanceof JSONArray) {
            JSONArray ja = (JSONArray)object;
            int length = ja.length();
            for (int i = 0; i < length; ++i) {
                sb.append(XML.toString(ja.opt(i), tagName == null ? "array" : tagName));
            }
            return sb.toString();
        }
        String string4 = string = object == null ? "null" : XML.escape(object.toString());
        return tagName == null ? "\"" + string + "\"" : (string.length() == 0 ? "<" + tagName + "/>" : "<" + tagName + ">" + string + "</" + tagName + ">");
    }
}


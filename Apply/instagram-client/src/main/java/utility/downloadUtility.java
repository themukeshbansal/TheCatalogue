package utility;

import Model.VideoEntity;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@NoArgsConstructor
public class downloadUtility {
    public boolean downloadVideo(final VideoEntity videoEntity) throws IOException {
        BufferedOutputStream outStream = null;
        InputStream is = null;
        if (!this.createDestination(videoEntity.getDestinationFolder())) {
            return false;
        }
        final File f = new File(
                videoEntity.getDestinationFolder()
                        + File.separator
                        + videoEntity.getName()
                        + "."
                        + videoEntity.getSuffix().toString().toLowerCase());
        try {
            int byteRead = 0;
            final URL url = new URL(videoEntity.getUrl());
            outStream = new BufferedOutputStream(new FileOutputStream(f));
            final URLConnection conn = url.openConnection();
            is = conn.getInputStream();
            final byte[] buf = new byte[(int)this.getFileSize(videoEntity.getUrl())];
            while ((byteRead = is.read(buf)) != -1) {
                outStream.write(buf, 0, byteRead);
            }
            outStream.flush();
        }catch(IOException e1) {
            throw new IOException();
        }
        catch (Exception e) {
            return false;
        }
        finally {
            try {
                is.close();
                outStream.close();
                System.gc();
            }
            catch (IOException ex) {}
        }
        try {
            is.close();
            outStream.close();
            System.gc();
        }
        catch (IOException ex2) {}
        return true;
    }

    private boolean createDestination(final String destinationDir) {
        try {
            final File fl = new File(destinationDir);
            if (!fl.exists()) {
                fl.mkdirs();
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public synchronized long getFileSize(final String urlString) throws IllegalAccessException {
        URL url = null;
        try {
            url = new URL(this.getFinalLocation(urlString));
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getContentLengthLong();
        }
        catch (IOException e2) {
            throw new IllegalAccessException();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public synchronized String getFinalLocation(final String address) throws IOException {
        final URL url = new URL(address);
        final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        final int status = conn.getResponseCode();
        if (status != 200 && (status == 302 || status == 301 || status == 303)) {
            final String newLocation = conn.getHeaderField("Location");
            return this.getFinalLocation(newLocation);
        }
        return address;
    }
}

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import javax.swing.SwingWorker;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadTask extends SwingWorker<Void, Void> {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";
    private String downloadURL;
    private String saveDirectory;
    private InstagramDownloader gui;

    public DownloadTask(InstagramDownloader gui, String downloadURL, String saveDirectory) {
        this.gui = gui;
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Document insta = Jsoup.connect(downloadURL).userAgent(USER_AGENT).get();
        String content = insta.select("meta[property=og:image]").first().attr("content");
        System.out.println(content);
        downloadImage(content);
        return null;
    }
    private void downloadImage(String path) throws IOException {
        String strImageName =
                path.substring(path.lastIndexOf("/") + 1);
        URL url = new URL(path);
        String imgName = path.substring(path.lastIndexOf("/"), path.lastIndexOf("jpg") + 3);
        String destName = this.saveDirectory + imgName;
        gui.setFileInfo(imgName, imgName.length());
        System.out.println("Dest name: " + destName);
        InputStream is = new BufferedInputStream(url.openStream());
        OutputStream out = new BufferedOutputStream(new FileOutputStream(destName));
        int n = 0;
        while (-1 != (n = is.read())) {
            out.write(n);
        }
        setProgress(100);
        out.close();
        is.close();

    }
    private static void createDir() throws IOException {
        Path dir = Paths.get("img");
        if (Files.notExists(dir)) {
            Files.createDirectory(dir);
        }
    }
}
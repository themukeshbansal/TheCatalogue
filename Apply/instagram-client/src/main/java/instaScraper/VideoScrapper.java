package instaScraper;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;

import java.io.IOException;

public abstract class VideoScrapper {
    Instagram4j instagram;

    VideoScrapper() throws IOException {
        instagram = Instagram4j.builder()
                .username("trellconsultant")
                .password("trellpassword").build();
        instagram.setup();
        instagram.login();
    }

    public abstract InstagramFeedResult getFeed(String keyword) throws IOException;
}

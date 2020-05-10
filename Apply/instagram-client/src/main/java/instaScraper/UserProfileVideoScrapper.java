package instaScraper;

import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;

import java.io.IOException;

public class UserProfileVideoScrapper extends VideoScrapper{
    public UserProfileVideoScrapper() throws IOException {
    }

    public InstagramFeedResult getFeed(String keyword) throws IOException {
//        InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramUserFeedRequest(keyword));
//        return tagFeed;
        return null;
    }
}

package instaScraper;

import org.brunocvcunha.instagram4j.requests.InstagramTagFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;

import java.io.IOException;

public class HashTagVideoScrapper extends VideoScrapper {
    public HashTagVideoScrapper() throws IOException {
    }

    public InstagramFeedResult getFeed(String keyword) throws IOException {
        InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramTagFeedRequest(keyword));
        return tagFeed;
    }

    public InstagramFeedResult getNextFeed(String keyword, String nextId) throws IOException {
        InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramTagFeedRequest(keyword, nextId));
        return tagFeed;
    }

}

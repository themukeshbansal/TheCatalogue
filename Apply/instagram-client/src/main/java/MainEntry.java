import Model.Suffix;
import Model.VideoEntity;
import instaScraper.HashTagVideoScrapper;
import org.brunocvcunha.instagram4j.requests.payload.ImageMeta;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import utility.csvUtility;
import utility.downloadUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MainEntry {
    public static void main(String[] args) throws IOException {

        // A thread pool video downloader for tag..
        HashTagVideoScrapper hashTagVideoScrapper = new HashTagVideoScrapper();
        String tagKeyword = "video";
        InstagramFeedResult feedResults = hashTagVideoScrapper.getFeed(tagKeyword);
//        InstagramFeedResult tagFeed = (InstagramFeedResult) App.instagram().sendRequest(
//                (InstagramRequest<?>) new InstagramUserFeedRequest(userResult.getUser().getPk()));
        feedResults.setAuto_load_more_enabled(true);
//        for (boolean down = true; down
//                && tagFeed.isMore_available(); tagFeed = (InstagramFeedResult) App.instagram().sendRequest(
//                (InstagramRequest<?>) new InstagramUserFeedRequest(userResult.getUser().getPk(),
//                        tagFeed.getNext_max_id(), 0L))) {
//            System.out.println(">>> Got " + tagFeed.getNum_results() + " results of " + profile.getName());
//
        List<String[]> videoItemRowStringList = new ArrayList<String[]>();

        videoItemRowStringList.add(new String[] {"Tag Keyword", "Like Count", "Download Url"});
        int count = 0;
        while (true){
            count++;
            for (InstagramFeedItem feedItem : feedResults.getItems()) {
                if (feedItem.getMedia_type() == 2) {
                    System.out.println(">>> Got " + feedItem.getId() + " results of " + feedItem.getMedia_type());
                    videoItemRowStringList.add(new String[] {tagKeyword, String.valueOf(feedItem.getLike_count()), extractMediaUrl(feedItem)});
                    //Multi Post
//            } else {
//                downloadNormalPost(feedItem);

                    //Normal Post
                }
            }
            if (!feedResults.isMore_available() || count > 500){
                break;
            } else {
                try{
                    feedResults = hashTagVideoScrapper.getNextFeed(tagKeyword, feedResults.getNext_max_id());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        csvUtility csvUtility = new csvUtility();
        csvUtility.writeDataAtOnce(String.format("/Users/mukesh.bansal/Public/%s.csv", tagKeyword), videoItemRowStringList);
    }
    // get video_versions : Available Url, width and height of the video.

    private static boolean downloadNormalPost(InstagramFeedItem feedResult) throws IOException {
        String url = extractMediaUrl(feedResult);
        String name = new StringBuilder(String.valueOf(feedResult.getPk())).toString();
        downloadUtility downloadUtility = new downloadUtility();
        downloadUtility.downloadVideo(
                VideoEntity.builder()
                        .name(name)
                        .destinationFolder("/Users/mukesh.bansal/Public/Videos")
                        .url(url)
                        .suffix(Suffix.MP4)
                        .build()
        );
        return true;
    }

    public static String extractMediaUrl(InstagramFeedItem feedResult) {
        String url = "";
        if (feedResult.getMedia_type() != 1) {
            List<ImageMeta> videoVersions = feedResult.getVideo_versions();
            for (ImageMeta videoVersion: videoVersions
                 ) {
                url = videoVersion.getUrl();
            }
        }
        return url;
    }
}
// Use next_max_id to fetch the next set of results..
// media_type = 2 is video

// A thread pool Video downloader per usernames..
/**
 * User Wise Feed Search
 * Tag Wise Feed Search
 *
 * After Getting feed results. Fetching the next set of results and the next.
 * For each set , getting the video links and other meta information
 *
 * Scraping the data and storing a unique reference of each record.
 * Using those to download and mark true for downloaded content.
 * 
 */
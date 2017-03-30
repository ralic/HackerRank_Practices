//package org.raliclo.Facebook_APIs;
//
//import facebook4j.*;
//import facebook4j.auth.AccessToken;
//import facebook4j.conf.ConfigurationBuilder;
//import facebook4j.internal.http.RequestMethod;
//import facebook4j.internal.org.json.JSONArray;
//import facebook4j.internal.org.json.JSONObject;
//import facebook4j.json.DataObjectFactory;
//
//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * Created by raliclo on 23/03/2017.
// */
//public class FB_API_Tests {
//    public static void main(String[] args) throws Exception {
//
//        // Configuration
//
//        // Timer for Speed Test
//        long speedX = System.currentTimeMillis();
//        String clientURL = "https://www.facebook.com/dialog/oauth";
//        String redirect_uri = "https://www.facebook.com/connect/login_success.html";
//        String appId = "1893798130904502";
//        String appSecret = "4a4027a8af68be1230dab58390113f41";
//        String appToken = "970662847e117eb20a57d46dc3e0c29a";
//        //https://developers.facebook.com/docs/facebook-login/permissions
//        String access_scope = "public_profile,user_friends,email";
//        String display = "popup";
//        String response_type = "token";
////        Getting Facebook Instance
////        At first it is necessary to acquire Facebook instance to use Facebook4J.
////        You can get Facebook instance in FacebookFactory.getInstance().
//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true)
//                .setOAuthAppId(appId)
//                .setOAuthAppSecret(appSecret)
//                .setOAuthAccessToken(appToken);
////                .setClientURL(clientURL)
////                .setOAuthCallbackURL(redirect_uri);
//
//        Facebook facebook = new FacebookFactory().getInstance();
////        If App ID / App Secret / access token / access permission are listed in facebook4j.properties then, they are set
////        in Facebook instance given back.
////                See Configuration | Facebook4J - A Java library for the Facebook Graph API for the detail.
////        When they are not listed, it is setable later as follows:
//
//        //https://developers.facebook.com/docs/marketing-api/access
//        String accessToken = "Development";
//        facebook.setOAuthAppId(appId, appSecret);
//        facebook.setOAuthPermissions(access_scope);
//        facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
////        OAuth support
////        Getting User Access Token
////        It is possible to authenticate users using Facebook accounts with your web application.
////                An example implementation is available at https://github.com/roundrop/facebook4j-oauth-example .
//
////        Getting App Access Token
////        You can get App Access Token via Facebook.getOAuthAppAccessToken() method.
//
//
//        AccessToken respToken = facebook.getOAuthAppAccessToken();
//        System.out.println(respToken);
////        Getting Page Access Token
////        You can get Page Access Token as below:
//
//        ResponseList<Account> accounts = facebook.getAccounts();
//        Account yourPageAccount = accounts.get(0);  // if index 0 is your page account.
//        String pageAccessToken = yourPageAccount.getAccessToken();
////        Getting Device Access Token
////        With Facebook Login for Devices people can easily and safely log into your apps and services with their
////        Facebook account on devices with limited input or display capabilities.
////        (See Facebook’s Documentation:Facebook Login for Devices )
////        An example implementation is available at https:
////github.com/roundrop/facebook4j-oauth-example .
//
////        Extending expiration of an Access Token
////        (See Facebook’s Documentation:Expiration and Extension of Access Tokens )
////        You can extend Access Token’s expiration as below:
//
//        String shortLivedToken = "your-short-lived-token";
//        AccessToken extendedToken = facebook.extendTokenExpiration(shortLivedToken);
//        System.out.println(extendedToken);
////        Publishing a message
////        You can publish a message via Facebook.postStatusMessage() method.
//
//        facebook.postStatusMessage("Hello World from Facebook4J.");
//
////        Publishing a link
////        You can publish a link via Facebook.postFeed() method.
////        Facebook.postFeed();
//        PostUpdate post = null;
//        post = new PostUpdate(new URL("http://facebook4j.org"))
//                .picture(new URL("http://facebook4j.org/images/hero.png"))
//                .name("Facebook4J - A Java library for the Facebook Graph API")
//                .caption("facebook4j.org")
//                .description("Facebook4J is a Java library for the Facebook Graph API.");
//
//        facebook.postFeed(post);
////        Facebook.postLink() method is simple way to post.
////            Facebook.postLink();
//        facebook.postLink(new URL("http://facebook4j.org"));
//        facebook.postLink(new URL("http://facebook4j.org"), "A Java library for the Facebook Graph API");
//
//        //        Getting News Feed
////        Facebook.getHome() returns a List of user’s latest News Feed.
////        Facebook.getHome();
////        ResponseList<Post> feed = facebook.getHome();
////        Like
////        You can like a Post, Photo, …via Facebook.like ****() methods.
////
////                facebook.likePost(postId);
////        Also, You can unlike a Post, Photo, …via Facebook.unlike ****() methods.
////
////                facebook.unlikePost(postId);
////        Publising a comment
////        You can comment a Post, Photo, …via Facebook.comment ****() methods.
////
////                facebook.commentPhoto(photoId, "It's a nice photo!");
////        Searching
////        You can search for Posts, Users, …via Facebook.search ****() methods.
//
////                Search for public Posts
//        ResponseList results = facebook.searchPosts("watermelon");
////        Search for Users
//        results = facebook.searchUsers("mark");
//        System.out.println((ResponseList<User>) results);
////        Search for Events
//        results = facebook.searchEvents("conference");
//        System.out.println((ResponseList<Event>) results);
////        Search for Groups
//        results = facebook.searchGroups("programming");
//        System.out.println((ResponseList<Group>) results);
//
////        Search for Places
//// Search by name
//        results = facebook.searchPlaces("coffee");
//        System.out.println((ResponseList<Place>) results);
//
//// You can narrow your search to a specific location and distance
//        GeoLocation center = new GeoLocation(37.76, -122.427);
//        int distance = 1000;
//
//        results = facebook.searchPlaces("coffee", center, distance);
//        System.out.println(results);
////        Search for Checkins
//// you or your friend's latest checkins, or checkins where you or your friends have been tagged
//        results = facebook.searchCheckins();
//        System.out.println((ResponseList<Checkin>) results);
////        Search for Locations
//// To search for objects near a geographical location
//        results = facebook.searchLocations(center, distance);
//        System.out.println((ResponseList<Location>) results);
//
//// To search for objects at a particular place
//        String placeId = "166793820034304";
//        results = facebook.searchLocations(placeId);
//        System.out.println((ResponseList<Location>) results);
////        Executing FQL
////        You can execute FQL via Facebook.executeFQL() method.
////                Also you can execute multiple FQL in one call via Facebook.executeMultiFQL() method.
//
//// Single FQL
//        String query = "SELECT uid2 FROM friend WHERE uid1=me()";
//        JSONArray jsonArray = facebook.executeFQL(query);
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            System.out.println(jsonObject.get("uid2"));
//        }
//
//// Multiple FQL
//        Map<String, String> queries = new HashMap<>();
//        queries.put("all friends", "SELECT uid2 FROM friend WHERE uid1=me()");
//        queries.put("my name", "SELECT name FROM user WHERE uid=me()");
//        Map<String, JSONArray> result = facebook.executeMultiFQL(queries);
//        JSONArray allFriendsJSONArray = result.get("all friends");
//        for (int i = 0; i < allFriendsJSONArray.length(); i++) {
//            JSONObject jsonObject = allFriendsJSONArray.getJSONObject(i);
//            System.out.println(jsonObject.get("uid2"));
//        }
//        JSONArray myNameJSONArray = result.get("my name");
//        System.out.println(myNameJSONArray.getJSONObject(0).get("name"));
////        Executing Batch Requests
////        You can execute Batch Requests via Facebook.executeBatch() method.
//
//// Executing "me" and "me/friends?limit=50" endpoints
//        BatchRequests<BatchRequest> batch = new BatchRequests<BatchRequest>();
//        batch.add(new BatchRequest(RequestMethod.GET, "me"));
//        batch.add(new BatchRequest(RequestMethod.GET, "me/friends?limit=50"));
//        List<BatchResponse> resultsBatch = new ArrayList<>();
//        resultsBatch = facebook.executeBatch(batch);
//        System.out.println(resultsBatch);
//        BatchResponse result1 = resultsBatch.get(0);
//        BatchResponse result2 = resultsBatch.get(1);
//
//// You can get http status code or headers
//        int statusCode1 = result1.getStatusCode();
//        String contentType = result1.getResponseHeader("Content-Type");
//        System.out.println(statusCode1 + " " + contentType);
//// You can get body content via as****() method
//        String jsonString = result1.asString();
//        System.out.println(jsonString);
//        JSONObject jsonObject = result1.asJSONObject();
//        System.out.println(result1);
//        ResponseList<JSONObject> responseList = result2.asResponseList();
//        System.out.println(responseList);
//
//// You can map json to java object using DataObjectFactory#create****()
//        User user = DataObjectFactory.createUser(jsonString);
//        Friend friend1 = DataObjectFactory.createFriend(responseList.get(0).toString());
//        Friend friend2 = DataObjectFactory.createFriend(responseList.get(1).toString());
//
////        You can attach a binary data to batch request as follows:
//
//        batch = new BatchRequests<>();
//        Media file = new Media(new File("...image.png"));
//        BatchAttachment attachment = new BatchAttachment("file", file);
//        batch.add(new BatchRequest(RequestMethod.POST, "me/photos")
//                .body("message=My photo")
//                .attachedFile(attachment));
////        Executing Raw API(setting the endpoint on your own)
////        You can execute the API endpoint that you want to run via Facebook.call **() method.
//
//// GET
//        RawAPIResponse res = facebook.callGetAPI("me");
//        jsonObject = res.asJSONObject();
//        String id = jsonObject.getString("id");
//
//// POST
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("message", "hello");
//        res = facebook.callPostAPI("me/feed", params);
//
//// DELETE
//        res = facebook.callDeleteAPI("123456/likes");
//        if (res.isBoolean()) {
//            System.out.println(res.asBoolean());
//        }
////        You can execute the API endpoint that is not supported by Facebook4J via Facebook.call **() method.
////
////                Reading options
////        You can set various reading options to the method that Reading object includes in arguments.
////
////                Selecting specific fields
////        You can choose the fields you want returned via Reading.fields(“fieldName1, fieldName2,…”).
//
//// Getting user's email address only
//        user = facebook.getUser(facebook.searchUsers("mark").get(0).getId(),
//                new Reading().fields("email"));
////        limit / offset
//// Getting 1st-10th results
//        results = facebook.searchPosts("watermelon", new Reading().limit(10));
//        System.out.println((ResponseList<Post>) results);
//
//// Getting 11th-20th results
//        results = facebook.searchPosts("watermelon", new Reading().limit(10).offset(10));
//        System.out.println((ResponseList<Post>) results);
////        until / since
////        until / since values can be a unix timestamp or any date accepted by PHP’s strtotime format.
//
//        results = facebook.searchPosts("watermelon", new Reading().until("yesterday"));
//        System.out.println((ResponseList<Post>) results);
////        Pagination
////        You can get next/previous page with Paging object in results via Facebook.fetchNext() / Facebook.fetchPrevious()
////        methods.
//
//        ResponseList page1 = facebook.getQuestionOptions("https://graph.facebook.com/338689702832185/options");
//        System.out.println(page1);
//
//// Getting Next page
//        Paging paging1 = page1.getPaging();
//        ResponseList page2 = facebook.fetchNext(paging1);
//        System.out.println(page2);
//// Getting Previous page
//        Paging paging2 = page2.getPaging();
//        page1 = facebook.fetchPrevious(paging2);
//        System.out.println(page1);
//        // Report for Speed Test
//        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");
//
//    }
//}

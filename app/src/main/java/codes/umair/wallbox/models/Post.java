package codes.umair.wallbox.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 Created by Umair Ayub on 17 Sept 2019.
 */
public class Post {

    @SerializedName("likes")
    @Expose
    private int likes;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("views")
    @Expose
    private int views;
    @SerializedName("previewWidth")
    @Expose
    private int previewWidth;
    @SerializedName("webformatHeight")
    @Expose
    private int webformatHeight;
    @SerializedName("webformatWidth")
    @Expose
    private int webformatWidth;
    @SerializedName("previewHeight")
    @Expose
    private int previewHeight;
    @SerializedName("downloads")
    @Expose
    private int downloads;
    @SerializedName("previewURL")
    @Expose
    private String previewURL;
    @SerializedName("webformatURL")
    @Expose
    private String webformatURL;
    @SerializedName("imageWidth")
    @Expose
    private int imageWidth;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("userImageURL")
    @Expose
    private String userImageURL;
    @SerializedName("imageHeight")
    @Expose
    private int imageHeight;
    @SerializedName("imageURL")
    @Expose
    private int imageURL;

    /**
     * @return The likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * @param likes The likes
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * @return The previewHeight
     */
    public int getPreviewHeight() {
        return previewHeight;
    }

    /**
     * @param previewHeight The previewHeight
     */
    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }

    /**
     * @return The tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }


    /**
     * @return The views
     */
    public int getViews() {
        return views;
    }

    /**
     * @param views The views
     */
    public void setViews(int views) {
        this.views = views;
    }

    /**
     * @return The previewWidth
     */
    public int getPreviewWidth() {
        return previewWidth;
    }

    /**
     * @param previewWidth The previewWidth
     */
    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }


    /**
     * @return The webformatHeight
     */
    public int getWebformatHeight() {
        return webformatHeight;
    }

    /**
     * @return The webformatWidth
     */
    public int getWebformatWidth() {
        return webformatWidth;
    }


    /**
     * @return The imageURL
     */
    public int getImageURL() {
        return imageURL;
    }

    /**
     * @return The downloads
     */
    public int getDownloads() {
        return downloads;
    }

    /**
     * @param downloads The downloads
     */
    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    /**
     * @return The previewURL
     */
    public String getPreviewURL() {
        return previewURL;
    }

    /**
     * @param previewURL The previewURL
     */
    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    /**
     * @return The webformatURL
     */
    public String getWebformatURL() {
        return webformatURL;
    }

    /**
     * @param webformatURL The webformatURL
     */
    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    /**
     * @return The imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param imageWidth The imageWidth
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @return The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return The user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The userImageURL
     */
    public String getUserImageURL() {
        return userImageURL;
    }

    /**
     * @param userImageURL The userImageURL
     */
    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    /**
     * @return The imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param imageHeight The imageHeight
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

}
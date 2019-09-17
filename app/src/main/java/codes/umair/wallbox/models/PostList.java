package codes.umair.wallbox.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostList {

    @SerializedName("totalPosts")
    @Expose
    private int totalPosts;
    @SerializedName("hits")
    @Expose
    private List<Post> hits = null;
    @SerializedName("total")
    @Expose
    private int total;

    /**
     * @return The totalPosts
     */
    public int getTotalPosts() {
        return totalPosts;
    }

    /**
     * @param totalPosts The totalPosts
     */
    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    /**
     * @return The hits
     */
    public List<Post> getPosts() {
        return hits;
    }

    /**
     * @param hits The hits
     */
    public void setPosts(List<Post> hits) {
        this.hits = hits;
    }

    /**
     * @return The total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(int total) {
        this.total = total;
    }

}
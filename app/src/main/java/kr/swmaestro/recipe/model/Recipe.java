package kr.swmaestro.recipe.model;

/**
 * Created by lk on 2015. 8. 2..
 */
public class Recipe {

    private String title;       // Recipe Title
    private int id;             // Recipe Id
    private String imageUrl;    // Recipe Thumbnail URL
    private String wasLike;     // Like Id

    public Recipe(String title, String id, String imageUrl, String wasLike) {
        this.title = title;
        this.id = Integer.parseInt(id);
        this.imageUrl = imageUrl;
        this.wasLike = wasLike;
    }

    public String getTitle() { return title; }

    public int getItemId() { return id; }

    public String getImageUrl() { return imageUrl; }

    public String getWasLike(){ return wasLike; }

    public void setWasLike(String id){ this.wasLike = id; }

    @Override
    public String toString() { return id+""; }
}

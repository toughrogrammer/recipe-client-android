package kr.swmaestro.recipe.model;

/**
 * Created by lk on 2015. 8. 2..
 */
public class Recipe {

    private String title;
    private int id;
    private String imageurl;
    private String wasLike;

    public Recipe(String title, String id, String imageurl, String wasLike) {
        this.title = title;
        this.id = Integer.parseInt(id);
        this.imageurl = imageurl;
        this.wasLike = wasLike;
    }

    public String getTitle() {
        return title;
    }

    public int getItemId() {
        return id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getWasLike(){
        return wasLike;
    }

    public void setWasLike(String id){
        this.wasLike = id;
    }

    @Override
    public String toString() {
        return id+"";
    }


}

package kr.swmaestro.recipe.model;

/**
 * Created by lk on 2015. 8. 2..
 */
public class Recipe {

    private String title;
    private int id;
    private String imageurl;

    public Recipe(String title, String id, String imageurl) {
        this.title = title;
        this.id = Integer.parseInt(id);
        this.imageurl = imageurl;
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

    @Override
    public String toString() {
        return id+"";
    }
}

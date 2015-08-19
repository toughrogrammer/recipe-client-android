package kr.swmaestro.recipe.util;

/**
 * Created by lk on 2015. 8. 15..
 */
public class AppSetting {

    /**
     * Server URL Setting
     */
    final public static String serverUrl = "http://recipe-main.herokuapp.com";
    final public static String tokenUrl = serverUrl + "/auth/me";
    final public static String recipeUrl = serverUrl + "/recipes";
    final public static String likeUrl = serverUrl + "/likes";

    /**
     * Font Setting
     * You can modify the font to put the assets folder
     */

    final public static String logoFont = "Nanumbut.ttf";
    final public static String appFont = "NanumBarunGothic.ttf";
    final public static String appFontBold = "NanumBarunGothicBold.ttf";
}

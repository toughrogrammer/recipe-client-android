package kr.swmaestro.recipe.model;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by lk on 2015. 8. 2..
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{

    private LruCache<String, Bitmap> mMemory;


    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    private static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    public LruBitmapCache(int maxSize) { super(maxSize); }

    @Override
    public Bitmap getBitmap(String url) { return get(url); }

    @Override
    public void putBitmap(String url, Bitmap bitmap) { put(url,bitmap); }
}

package kr.swmaestro.recipe.model;

import java.util.ArrayList;

/**
 * Created by lk on 2015. 8. 24..
 */
public class Feelings {

    private ArrayList<String> feels;
    private ArrayList<String> ckfeels;

    public Feelings(){
        feels = new ArrayList<>();
        feels.add("");
        feels.add("spicy");
        feels.add("sweety");
        feels.add("salty");
        feels.add("glutin");
        feels.add("fatty");
        feels.add("crunch");
        feels.add("cool");
        feels.add("clean");
        feels.add("soft");
        feels.add("hot");
        feels.add("fresh");
    }

    public int getSize(){
        return feels.size();
    }

    public void setCkfeels(boolean[] ckdata){
        ckfeels = new ArrayList<>();
        for(int i=0; i<feels.size(); i++){
            if(ckdata[i])
                ckfeels.add(feels.get(i));
        }
    }

    public ArrayList<String> getFeels(){
        return feels;
    }

    public ArrayList<String> getCkfeels(){
        return ckfeels;
    }

}

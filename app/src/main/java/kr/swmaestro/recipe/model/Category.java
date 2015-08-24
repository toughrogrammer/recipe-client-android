package kr.swmaestro.recipe.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lk on 2015. 8. 25..
 */
public class Category {

    private ArrayList<Integer> code;
    private ArrayList<String> title;
    private ArrayList<Integer> ckCategory;

    public Category(){
        code = new ArrayList<>();
        code.add(48196);
        code.add(48197);
        code.add(48198);
        code.add(48199);
        code.add(48200);
        code.add(48201);
        code.add(48202);
        code.add(48203);
        code.add(48204);
        code.add(48205);
        code.add(48206);
        code.add(48207);
        code.add(48208);
        code.add(48209);
        code.add(48210);
        code.add(48211);
        code.add(48212);
        code.add(48213);
        code.add(48214);
        code.add(48215);
        code.add(48216);
        code.add(48217);
        code.add(48218);
        code.add(48219);
        code.add(48220);
        code.add(48221);
        code.add(48222);
        code.add(48223);
        code.add(48224);
        code.add(48225);
        code.add(48226);
        code.add(48228);
        code.add(48234);
        code.add(48235);
        code.add(48236);
        code.add(48392);
        code.add(48393);
        code.add(48394);
        code.add(48395);
        code.add(48397);
        code.add(48399);
        code.add(48400);
        code.add(48401);
        code.add(48402);
        code.add(48403);
        code.add(48404);
        code.add(48233);
        code.add(48172);
        code.add(48196);
        code.add(48197);
        code.add(48198);
        code.add(48199);
        code.add(48200);
        code.add(48201);
        code.add(48202);
        code.add(48203);
        code.add(48204);
        code.add(48205);
        code.add(48206);
        code.add(48207);
        code.add(48208);
        code.add(48209);
        code.add(48210);
        code.add(48211);
        code.add(48212);
        code.add(48213);
        code.add(48214);
        code.add(48215);
        code.add(48216);
        code.add(48217);
        code.add(48218);
        code.add(48219);
        code.add(48220);
        code.add(48221);
        code.add(48222);
        code.add(48223);
        code.add(48224);
        code.add(48225);
        code.add(48226);
        code.add(48228);
        code.add(48234);
        code.add(48235);
        code.add(48236);
        code.add(48392);
        code.add(48393);
        code.add(48394);
        code.add(48395);
        code.add(48397);
        code.add(48399);
        code.add(48400);
        code.add(48401);
        code.add(48402);
        code.add(48403);
        code.add(48404);
        code.add(48233);
        code.add(48172);

        title = new ArrayList<>();
        title.add("밥류");
        title.add("죽류");
        title.add("면류");
        title.add("만두");
        title.add("국/탕");
        title.add("찌개/전골");
        title.add("구이");
        title.add("조림");
        title.add("볶음");
        title.add("무침");
        title.add("튀김");
        title.add("부침");
        title.add("찜/삶기");
        title.add("냉채");
        title.add("김치");
        title.add("젓갈");
        title.add("장아찌");
        title.add("피클");
        title.add("샐러드");
        title.add("수프");
        title.add("피자");
        title.add("스파게티/파스타");
        title.add("스테이크");
        title.add("잼");
        title.add("드레싱/소스");
        title.add("떡");
        title.add("한과");
        title.add("빵");
        title.add("샌드위치/토스트");
        title.add("무스/푸딩/크림");
        title.add("과자");
        title.add("차/다류");
        title.add("장류");
        title.add("조미료/향신료");
        title.add("가루류");
        title.add("주스");
        title.add("건강음료");
        title.add("스무디/쉐이크");
        title.add("와인/포도주");
        title.add("맥주");
        title.add("탁주/동동주/전통주");
        title.add("기타주류");
        title.add("빙수");
        title.add("아이스크림");
        title.add("치즈");
        title.add("요구르트");
        title.add("기타음료류/빙과류/유제품");
        title.add("기타 요리별 레시피");
        title.add("밥류");
        title.add("죽류");
        title.add("면류");
        title.add("만두");
        title.add("국/탕");
        title.add("찌개/전골");
        title.add("구이");
        title.add("조림");
        title.add("볶음");
        title.add("무침");
        title.add("튀김");
        title.add("부침");
        title.add("찜/삶기");
        title.add("냉채");
        title.add("김치");
        title.add("젓갈");
        title.add("장아찌");
        title.add("피클");
        title.add("샐러드");
        title.add("수프");
        title.add("피자");
        title.add("스파게티/파스타");
        title.add("스테이크");
        title.add("잼");
        title.add("드레싱/소스");
        title.add("떡");
        title.add("한과");
        title.add("빵");
        title.add("샌드위치/토스트");
        title.add("무스/푸딩/크림");
        title.add("과자");
        title.add("차/다류");
        title.add("장류");
        title.add("조미료/향신료");
        title.add("가루류");
        title.add("주스");
        title.add("건강음료");
        title.add("스무디/쉐이크");
        title.add("와인/포도주");
        title.add("맥주");
        title.add("탁주/동동주/전통주");
        title.add("기타주류");
        title.add("빙수");
        title.add("아이스크림");
        title.add("치즈");
        title.add("요구르트");
        title.add("기타음료류/빙과류/유제품");
        title.add("기타 요리별 레시피");

    }

    public ArrayList<Integer> setCkCategory(boolean[] ckdata){
        ckCategory = new ArrayList<>();
        for(int i=0; i<48; i++){
            if(ckdata[i])
                ckCategory.add(code.get(i));
        }
        return ckCategory;
    }

    public int getSize(){
        return code.size();
    }

    public String getTitle(int i){
        return title.get(i);
    }
}

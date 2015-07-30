package kr.swmaestro.recipe.model;

import java.util.HashMap;

/**
 * Created by lk on 2015. 7. 30..
 */
public class ErrorMap extends HashMap<String,String> {

    public ErrorMap(){
        this.put("Error.Passport.Password.Invalid","패스워드 형식이 잘못되었습니다.");
        this.put("Error.Passport.Password.Wrong","패스워드가 다릅니다.");
        this.put("Error.Passport.Password.NotSet","패스워드를 입력해주세요.");
        this.put("Error.Passport.Username.NotFound","이름을 찾을 수 없습니다.");
        this.put("Error.Passport.User.Exists","이미 사용중인 이름입니다.");
        this.put("Error.Passport.Email.NotFound","이메일을 찾을 수 없습니다.");
        this.put("Error.Passport.Email.Missing","이메일 형식이 잘못되었습니다.");
        this.put("Error.Passport.Email.Exists","이미 가입한 이메일입니다.");
        this.put("Error.Passport.Username.Missing","이름 형식이 잘못되었습니다.");
        this.put("Error.Passport.Password.Missing","비밀번호 형식이 잘못되었습니다.");
        this.put("Error.Passport.Generic","입력 형식이 잘못되었습니다.");
    }
}

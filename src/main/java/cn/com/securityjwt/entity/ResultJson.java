package cn.com.securityjwt.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author huwenhu
 * @Date 2019/8/7 11:28
 **/
@Data
@AllArgsConstructor
public class ResultJson {

    private int nFailCode;
    private String strFailMess;


    public static ResultJson build(int nFailCode, String strFailMess){
        return new ResultJson(nFailCode, strFailMess);
    }

    public String toJSONString(){
        return JSONObject.toJSONString(this);
    }

    public static void main(String[] args){
        String s = ResultJson.build(100, "sdad").toJSONString();
        System.out.println(s);
    }
}

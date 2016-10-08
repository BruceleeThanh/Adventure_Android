package studio.crazybt.adventure.libs;

import java.util.HashMap;

/**
 * Created by Brucelee Thanh on 09/10/2016.
 */

public class ApiParams extends HashMap<String, String> {

    public ApiParams putParam(String key, Object value){
        this.put(key, value.toString());
        return this;
    }
    public HashMap<String, String> build(){
        return this;
    }

    public static ApiParams getBuilder(){
        return new ApiParams();
    }
}

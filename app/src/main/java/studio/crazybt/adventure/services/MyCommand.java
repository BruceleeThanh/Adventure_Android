package studio.crazybt.adventure.services;

import android.content.Context;

import com.android.volley.Request;

import java.util.ArrayList;

/**
 * Created by Brucelee Thanh on 12/10/2016.
 */

public class MyCommand<T>  {
    private ArrayList<Request<T>> requestArrayList = new ArrayList<>();
    private Context context;

    public MyCommand(Context context) {
        this.context = context;
    }

    public void add(Request<T> request){
        requestArrayList.add(request);
    }

    public void remove(Request<T> request){
        requestArrayList.add(request);
    }

    public void execute(){
        for(Request<T> request : requestArrayList){
            MySingleton.getInstance(this.context).addToRequestQueue(request);
        }
    }
}

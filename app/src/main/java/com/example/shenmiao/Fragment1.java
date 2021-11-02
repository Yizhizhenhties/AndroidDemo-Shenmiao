package com.example.shenmiao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Fragment1 extends Fragment {

    private String TAG= "";
    private View view;
    private List<News> newsList;
    private NewsAdapter mAdapter;
    private Handler handler;
    private RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取fragment的layout
        view = inflater.inflate(R.layout.myfragment1, container, false);
        newsList=new ArrayList<News>();
        mRecyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(manager);
        connectNews(view);

        return view;
    }
    private void connectNews(View view) {
        newsList = new ArrayList<>();
        Observable.create(new ObservableOnSubscribe<List<News>>() {
            @Override
            public void subscribe(ObservableEmitter<List<News>> emitter) throws Exception {
                for(int j=1;j<=2;j++)
                {
                    Document document = Jsoup.connect("https://search.sina.com.cn/?q=%e7%96%ab%e8%8b%97&c=news&by=&from=&t=&sort=rel&range=title&page="+Integer.toString(j)).get();
                    Elements box = document.getElementsByClass("box-result clearfix");
                    for (int i = 0; i < box.size(); i++) {
                        News one_new = new News();
                        String title = box.get(i).select("h2").select("a").text();
                        if (title.length()>25)
                        {
                            title = title.substring(0,25)+"...";
                        }
                        String uri = box.get(i).select("h2").select("a").attr("href");
                        String autor = box.get(i).select("h2").select("span").text();
                        String img = box.get(i).getElementsByClass("r-img").select("a").select("img").attr("src");
                        if(img.equals(""))
                        {
                            continue;
                        }
                        one_new.setDesc(title);
                        one_new.setAutor(autor);
                        one_new.setImage(img);
                        one_new.setNewsUrl(uri);
                        newsList.add(one_new);
                    }
                }
                emitter.onNext(newsList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<List<News>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<News> news) {
                        mAdapter = new NewsAdapter(view.getContext(), newsList);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

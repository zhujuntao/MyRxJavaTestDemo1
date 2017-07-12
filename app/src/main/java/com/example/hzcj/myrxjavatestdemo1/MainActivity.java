package com.example.hzcj.myrxjavatestdemo1;

import android.annotation.TargetApi;
import android.database.Observable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hzcj.myrxjavatestdemo1.bean.Student;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d("zhu", "---------onCompleted-----------");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("zhu", "---------onError-----------" + e);
            }

            @Override
            public void onNext(String s) {
                Log.d("zhu", "---------onNext-----------" + s);
            }
        };

        rx.Observable<String> observable = rx.Observable.create(new rx.Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(observer);
//        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello");
//                subscriber.onNext("Hi");
//                subscriber.onNext("Aloha");
//                subscriber.onCompleted();
//            }
//        });
//        Observable observable= rx.Observable.just("Hello", "Hi", "Aloha");
//        Subscriber<String> subscriber=new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                Log.d("zhu","-------onCompleted---------------");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d("zhu","-------onError---------------");
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d("zhu","-------onNext---------------"+s);
//            }
//        };
//        Observable<String> myobservable=Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("张三");
//                subscriber.onCompleted();
//            }
//        });
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("zhu", "-------onNextAction---------------" + s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("zhu", "-------onErrorAction---------------" + throwable);
            }
        };
        Action0 onCompleted = new Action0() {
            @Override
            public void call() {
                Log.d("zhu", "-------onCompleted---------------");
            }
        };


        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompleted);


        String[] names = {"张三", "李四", "王二", "赵六"};
        rx.Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("zhu", "--- 打印字符串数组----onCompleted---------------" + s);
            }
        });

        // 由 id 取得图片并显示
        // 由指定的一个 drawable 文件 id drawableRes 取得图片，并显示在 ImageView 中，并在出现异常的时候打印 Toast 报错：
//      int drawableRes=getResources().getDrawable(R.drawable.baocun);
        final int drawableRes = R.drawable.baocun;
        final ImageView image = (ImageView) findViewById(R.id.img_test);
        rx.Observable.create(new rx.Observable.OnSubscribe<Drawable>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this.getApplicationContext(), drawableRes);
//                Drawable drawable=getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Error!-----drawable-----------", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        image.setImageDrawable(drawable);
                    }
                });

        //map 转换
        Student student1=new Student();
        student1.setName("奶茶");
        student1.setCourse("英语");
        student1.setSex("女");
        Student student2=new Student();
        student2.setName("旭光");
        student2.setCourse("语文");
        student2.setSex("男");
        final Student [] students={student1,student2};
        Subscriber<String> subscriber=new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d("zhu", "--- 打印map转换----onNext---------------" + s);
            }
        };

        rx.Observable.from(students).map(new Func1<Student, String>() {
            @Override
            public String call(Student student) {
                return student.getName();
            }
        }).subscribe(subscriber);
        //map 转换 打印学生的课目
        final List<String> courses=new ArrayList<>();
        Subscriber<Student> subscriberStudent=new Subscriber<Student>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student student) {

               courses.add(student.getCourse());
                for (int i = 0; i <courses.size() ; i++) {
                    Log.d("zhu", "--- 打印map转换成科目----onNext---------------"+courses.get(i));
                }

            }
        };
        rx.Observable.from(students).subscribe(subscriberStudent);

        //flatmap  创建及使用方式    打印学生的科目
       Subscriber<String>  subscriberFlatmap=new Subscriber<String>() {
           @Override
           public void onCompleted() {

           }

           @Override
           public void onError(Throwable e) {

           }

           @Override
           public void onNext(String s) {
               Log.d("zhu", "--- 打印flatmap转换成科目----onNext-----------"+s);
           }
       };

        rx.Observable.from(students).flatMap(new Func1<Student, rx.Observable<String>>() {
            @Override
            public rx.Observable<String> call(Student student) {
                String[]  str_flatmap =new String[students.length];
//                for (int i = 0; i < students.length; i++) {
////                    str_flatmap[i]=students[i].getSex();
//
//                }
//                str_flatmap[i]=student.getSex();
//                return rx.Observable.from(str_flatmap);
                return rx.Observable.just(student.getSex());
            }
        }).subscribe(subscriberFlatmap);





    }
}

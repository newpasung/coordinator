package com.scut.gof.coordinator.main.storage.model;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.scut.gof.coordinator.CooApplication;
import com.scut.gof.coordinator.main.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/18.
 */
@Table(name = "post")
public class Post extends Model {
    //七牛加后缀就能处理
    public static final String THUMBNAIL_MODE = "?imageView2/0/w/200/h/200";
    @Column(name = "postid")
    private long postid;
    @Column(name = "content")
    private String content;
    @Column(name = "creatorid")
    private long creatorid;
    @Column(name = "created_time")
    private long created_time;
    @Column(name = "type")
    private int type;
    @Column(name = "picthumbnail0")
    private String picthumbnail0;
    @Column(name = "pic0")
    private String pic0;
    @Column(name = "picthumbnail1")
    private String picthumbnail1;
    @Column(name = "pic1")
    private String pic1;
    @Column(name = "picthumbnail2")
    private String picthumbnail2;
    @Column(name = "pic2")
    private String pic2;
    @Column(name = "picthumbnail3")
    private String picthumbnail3;
    @Column(name = "pic3")
    private String pic3;
    @Column(name = "picthumbnail4")
    private String picthumbnail4;
    @Column(name = "pic4")
    private String pic4;
    @Column(name = "picthumbnail5")
    private String picthumbnail5;
    @Column(name = "pic5")
    private String pic5;
    @Column(name = "picthumbnail6")
    private String picthumbnail6;
    @Column(name = "pic6")
    private String pic6;
    @Column(name = "picthumbnail7")
    private String picthumbnail7;
    @Column(name = "pic7")
    private String pic7;
    @Column(name = "picthumbnail8")
    private String picthumbnail8;
    @Column(name = "pic8")
    private String pic8;
    @Column(name = "piccount")
    private int piccount;

    public static Post insertOrUpdate(JSONObject data) {
        Post post = null;
        try {
            long postid = data.getLong("postid");
            post = getPostById(postid);
            if (post == null) {
                post = new Post();
                post.postid = postid;
            }
            if (data.has("content")) {
                post.content = data.getString("content");
            }
            if (data.has("created_time")) {
                post.created_time = data.getLong("created_time");
            }
            if (data.has("creator")) {
                JSONObject creator = data.getJSONObject("creator");
                post.creatorid = creator.getLong("uid");
                User.insertOrUpdate(creator);
            }
            if (data.has("type")) {
                post.type = data.getInt("type");
            }
            if (data.has("pictures")) {
                JSONArray pictures = data.getJSONArray("pictures");
                for (int i = 0; i < pictures.length(); i++) {
                    switch (i) {
                        case 0: {
                            post.pic0 = pictures.getString(i);
                            post.picthumbnail0 = post.pic0 + THUMBNAIL_MODE;
                        }
                        break;
                        case 1: {
                            post.pic1 = pictures.getString(i);
                            post.picthumbnail1 = post.pic1 + THUMBNAIL_MODE;
                        }
                        break;
                        case 2: {
                            post.pic2 = pictures.getString(i);
                            post.picthumbnail2 = post.pic2 + THUMBNAIL_MODE;
                        }
                        break;
                        case 3: {
                            post.pic3 = pictures.getString(i);
                            post.picthumbnail3 = post.pic3 + THUMBNAIL_MODE;
                        }
                        break;
                        case 4: {
                            post.pic4 = pictures.getString(i);
                            post.picthumbnail4 = post.pic4 + THUMBNAIL_MODE;
                        }
                        break;
                        case 5: {
                            post.pic5 = pictures.getString(i);
                            post.picthumbnail5 = post.pic5 + THUMBNAIL_MODE;
                        }
                        break;
                        case 6: {
                            post.pic6 = pictures.getString(i);
                            post.picthumbnail6 = post.pic6 + THUMBNAIL_MODE;
                        }
                        break;
                        case 7: {
                            post.pic7 = pictures.getString(i);
                            post.picthumbnail7 = post.pic7 + THUMBNAIL_MODE;
                        }
                        break;
                        case 8: {
                            post.pic8 = pictures.getString(i);
                            post.picthumbnail8 = post.pic8 + THUMBNAIL_MODE;
                        }
                        break;
                        default:
                            break;
                    }
                }
                post.piccount = pictures.length();
            }
            post.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return post;
    }

    public static List<Post> insertOrUpdate(JSONArray array) {
        ActiveAndroid.beginTransaction();
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Post post = null;
            try {
                post = insertOrUpdate(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (post != null) {
                posts.add(post);
            }
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        return posts;
    }

    public static Post getPostById(long postid) {
        return new Select().from(Post.class).where("postid=" + postid).executeSingle();
    }

    public static List<Post> get10MyPosts() {
        Log.i("DESC_SQL", new Select().from(Post.class)
                .where("creatorid=" + UserManager.getUserid(CooApplication.getInstance()))
                .orderBy("created_time DESC")
                .toSql());
        return new Select().from(Post.class)
                .where("creatorid=" + UserManager.getUserid(CooApplication.getInstance()))
                .orderBy("created_time DESC")
                .limit(10)
                .execute();
    }

    public User getCreator() {
        return User.getUserById(this.creatorid);
    }

    public String getContent() {
        return content;
    }

    public long getCreated_time() {
        return created_time;
    }

    public long getCreatorid() {
        return creatorid;
    }

    public String getPic0() {
        return pic0;
    }

    public String getPic1() {
        return pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public String getPic5() {
        return pic5;
    }

    public String getPic6() {
        return pic6;
    }

    public String getPic7() {
        return pic7;
    }

    public String getPic8() {
        return pic8;
    }

    public int getPiccount() {
        return piccount;
    }

    public String getPicthumbnail0() {
        return picthumbnail0;
    }

    public String getPicthumbnail1() {
        return picthumbnail1;
    }

    public String getPicthumbnail2() {
        return picthumbnail2;
    }

    public String getPicthumbnail3() {
        return picthumbnail3;
    }

    public String getPicthumbnail4() {
        return picthumbnail4;
    }

    public String getPicthumbnail5() {
        return picthumbnail5;
    }

    public String getPicthumbnail6() {
        return picthumbnail6;
    }

    public String getPicthumbnail7() {
        return picthumbnail7;
    }

    public String getPicthumbnail8() {
        return picthumbnail8;
    }

    /**
     * 下面的两个函数利用七牛做了居中的缩略图,为了保证不变形，不进行裁剪
     */
    public String getHPreviewBigPic0() {
        return this.pic0 + "?imageView2/1/w/600/h/300";
    }

    public String getVPreviewBigPic0() {
        return this.pic0 + "?imageView2/1/w/300/h/600";
    }

    public String getPreviewBitPic0() {
        return this.pic0 + "?imageView2/0/w/500/h/500";
    }

    public long getPostid() {
        return postid;
    }

    public int getType() {
        return type;
    }

    public String getDisplayTime() {
        long delta = System.currentTimeMillis() - this.created_time;
        //发布超过两小时提示日期
        if (delta > 7200000) {
            GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance(Locale.CHINA);
            GregorianCalendar postCreatedData = new GregorianCalendar();
            postCreatedData.setTimeInMillis(created_time);
            StringBuilder builder = new StringBuilder();
            if (postCreatedData.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)) {
                builder.append(postCreatedData.get(Calendar.YEAR));
                builder.append("年");
            }
            if (postCreatedData.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
                builder.append(postCreatedData.get(Calendar.MONTH) + 1);
                builder.append("月");
            }
            if (postCreatedData.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)) {
                builder.append(postCreatedData.get(Calendar.DAY_OF_MONTH));
                builder.append("日");
            } else {
                builder.append("今天");
            }
            builder.append(postCreatedData.get(Calendar.HOUR_OF_DAY));
            builder.append("时");
            builder.append(postCreatedData.get(Calendar.MINUTE));
            builder.append("分");
            return builder.toString();
        } else if (delta > 3600000) {
            //两小时内
            return "两小时内";
        } else if (delta > 1800000) {
            return "一小时内";
        } else if (delta > 60000) {
            //大过一分钟
            return "半小时内";
        } else {
            return "一分钟内";
        }
    }

}

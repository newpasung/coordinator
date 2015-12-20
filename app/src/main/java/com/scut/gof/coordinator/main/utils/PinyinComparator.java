package com.scut.gof.coordinator.main.utils;

import com.scut.gof.coordinator.main.storage.model.User;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/12/17.
 */
public class PinyinComparator implements Comparator<User> {

    @Override
    public int compare(User lhs, User rhs) {
        if (!lhs.getName_pinyin().startsWith("#") && !rhs.getName_pinyin().startsWith("#")) {
            return lhs.getName_pinyin().compareTo(rhs.getName_pinyin());
        } else if (rhs.getName_pinyin().startsWith("#") && !lhs.getName_pinyin().startsWith("#")) {
            return -1;
        } else if (lhs.getName_pinyin().startsWith("#") && !rhs.getName_pinyin().startsWith("#")) {
            return 1;
        } else {
            return lhs.getName_pinyin().replace("#", "").compareTo(rhs.getName_pinyin().replace("#", ""));
        }
    }
}

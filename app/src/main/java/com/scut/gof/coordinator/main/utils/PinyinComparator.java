package com.scut.gof.coordinator.main.utils;

import com.scut.gof.coordinator.main.models.SimpleContact;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/12/17.
 */
public class PinyinComparator implements Comparator<SimpleContact> {

    @Override
    public int compare(SimpleContact lhs, SimpleContact rhs) {
        if (!lhs.getPinyin().startsWith("#") && !rhs.getPinyin().startsWith("#")) {
            return lhs.getPinyin().compareTo(rhs.getPinyin());
        } else if (rhs.getPinyin().startsWith("#") && !lhs.getPinyin().startsWith("#")) {
            return -1;
        } else if (lhs.getPinyin().startsWith("#") && !rhs.getPinyin().startsWith("#")) {
            return 1;
        } else {
            return lhs.getPinyin().replace("#", "").compareTo(rhs.getPinyin().replace("#", ""));
        }
    }
}

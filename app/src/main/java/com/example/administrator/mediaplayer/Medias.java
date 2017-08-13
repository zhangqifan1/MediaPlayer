package com.example.administrator.mediaplayer;

/**
 * Created by 张祺钒
 * on2017/8/13.
 */

public class Medias {
    String path;
    String name;

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Medias(String path, String name) {
        this.path = path;
        this.name = name;
    }
}

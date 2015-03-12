package com.moses.io.beams;

import java.util.List;

/**
 * Created by 丹 on 2015/1/12.
 */
public class ImgAlbum {
    private String albumsName;
    private List<ImgURL> picUrlSet;

    public void setAlbumsName(String albumsName) {
        this.albumsName = albumsName;
    }

    public List<ImgURL> getPicUrlSet() {
        return picUrlSet;
    }

    public void setPicUrlSet(List<ImgURL> picUrlSet) {
        this.picUrlSet = picUrlSet;
    }

    public String getAlbumsName() {
        return albumsName;
    }
}

package com.flurdy.grid.snaps.domain;

import com.google.api.client.googleapis.json.*;
import com.google.api.client.http.*;
import com.google.api.client.util.*;

import java.util.Collection;
import java.util.List;


public class PicasaWebProvider implements IProvider {

    @Override
    public List<String> findThumbnails(PhotoAlbum photoAlbum) {

		final String userId = findUserId( photoAlbum );
		final String albumId = findAlbumId( userId, photoAlbum );


        // TODO

		// final String albumURL = "http://picasaweb.google.com/data/feed/api/user/{userID}/albumid/{albumID}";

        return null;
    }

    private String findUserId(PhotoAlbum photoAlbum){
        return  photoAlbum.getSharingProvider().parsePicasaUsername(photoAlbum.getUrl());
    }

    private String findAlbumId(String userId, PhotoAlbum photoAlbum){

        // TODO

        return null;
    }

}

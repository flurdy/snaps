package com.flurdy.grid.snaps.domain;

//import com.google.api.client.googleapis.json.*;
//import com.google.api.client.http.*;
//import com.google.api.client.util.*;

import com.flurdy.grid.snaps.domain.picasa.PicasaUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;


public class PicasaWebProvider implements IProvider {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<String> findThumbnails(PhotoAlbum photoAlbum) {

		final String userId = findUserId( photoAlbum );
		final String albumId = findAlbumId( userId, photoAlbum );

        return pick3Thumbnails(userId,albumId);
    }

    private String findUserId(PhotoAlbum photoAlbum){
        return  photoAlbum.getSharingProvider().parsePicasaUsername(photoAlbum.getUrl());
    }

    private String findAlbumId(String userId, PhotoAlbum photoAlbum){
        final String album = photoAlbum.getSharingProvider().parsePicasaAlbumname(photoAlbum.getUrl());

        log.debug("Album?:"+album);

        // TODO

        return album;
    }

    private List<String> pick3Thumbnails(String userId, String albumId){

        final PicasaUrl albumURL = new PicasaUrl("feed/api/user/"+userId+"/albumid/"+albumId);

        log.debug("AlbumUrl?:"+albumURL);



        // TODO:


        return null;
    }

}

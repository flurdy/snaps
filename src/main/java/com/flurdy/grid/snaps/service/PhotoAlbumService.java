package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.*;
import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathOperations;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.GetMethod;


@Service
public class PhotoAlbumService extends AbstractService implements IPhotoAlbumService {

	@Autowired ITravellerService travellerService;

	@Resource
	protected Map<String,String> configurationMap;

	@Override
	public PhotoAlbum addAlbum(HolidayGroup holidayGroup, PhotoSharingProvider provider, String url) {

		final Traveller owner = travellerService.findCurrentTraveller();

		assert holidayGroup != null;
		assert provider != null;
		assert url != null;

		log.info("Traveller [" + owner.getSecurityDetail().getUsername()
				+ "] is adding album from provider ["+provider.name()
				+ "] to group ["+ holidayGroup.getGroupName()+"] with url: " + url);


		if( holidayGroup.isMember(owner)){

			if( provider.validUrl(url) ){

//				String providerId = provider.parseProviderId(url);

//				log.debug("PROVIDER ID IS "+providerId);

				PhotoAlbum album = new PhotoAlbum.Builder()
						.sharingProvider(provider)
						.url(url)
//						.providerId(providerId)
						.holidayGroup(holidayGroup)
						.owner(owner)
						.build();

				if( album.isValid() ){

					photoAlbumRepository.addAlbum(album);

					return album;

				} else {
					throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.INVALID_INPUT);
				}
			} else {
				throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.INVALID_INPUT);
			}

		} else {
			log.info("not member");
			throw new SnapAccessDeniedException(SnapAccessDeniedException.SnapAccessError.NOT_MEMBER);
		}

	}

	@Override
	public PhotoAlbum findPhotoAlbum( long holidayId, long albumId ){

		assert holidayId > 0;
		assert albumId > 0;

		final HolidayGroup holidayGroup = holidayGroupRepository.findHolidayGroup( holidayId );
		if( holidayGroup != null ){
			final Traveller traveller = travellerService.findCurrentTraveller();
			if( holidayGroup.isMember(traveller)){
				final PhotoAlbum photoAlbum = photoAlbumRepository.findAlbum(albumId);
				if( photoAlbum != null ){
					if( photoAlbum.getHolidayGroup().equals(holidayGroup) ){
						return photoAlbum;
					} else {
						log.info("Photo album was NOT for this holiday group");
						throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.PHOTO_ALBUM);
					}
				} else {
					log.info("Photo album not found");
					return null;
//					throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.PHOTO_ALBUM);
				}
			} else {
				log.info("not member");
				throw new SnapAccessDeniedException(SnapAccessDeniedException.SnapAccessError.NOT_MEMBER);
			}
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
		}

	}

	@Override
	public Collection<String> findThumbnails(final PhotoAlbum photoAlbum) {

		assert photoAlbum.getSharingProvider().equals(PhotoSharingProvider.FLICKR);
//		assert configurationMap.get("flickrApiKey") != null && configurationMap.get("flickrApiKey").trim().length()>0;

		Collection<String> thumbnails = new HashSet<String>();
		if(configurationMap.containsKey("flickrApiKey") && configurationMap.get("flickrApiKey") != null && configurationMap.get("flickrApiKey").trim().length()>0 ){

			final String photosetUrl = "http://www.flickr.com/services/rest/?method={method}" +
					"&api_key={api_key}&photoset_id={photoset_id}&media=photos&per_page=3&format=rest&privacy_filter=public%20photos";
			Map<String,String> parameters = new HashMap<String,String>(){{
				put("method","flickr.photosets.getPhotos");
				put("api_key",configurationMap.get("flickrApiKey"));
				put("photoset_id",photoAlbum.getSharingProvider().parseProviderId(photoAlbum.getUrl()));
			}};

			log.debug("API:"+configurationMap.get("flickrApiKey"));
			log.debug("ALBUM URL:"+photoAlbum.getUrl());
			log.debug("ID:"+parameters.get("photoset_id"));

			RestTemplate restTemplate = new RestTemplate();

			try {

				Source flickrResponse = restTemplate.getForObject( photosetUrl, Source.class, parameters );

//				log.debug("flickr response?:"+flickrResponse);
				XPathOperations xpathTemplate = new Jaxp13XPathTemplate();
				thumbnails = xpathTemplate.evaluate("//photo", flickrResponse, new NodeMapper(){
					public Object mapNode(Node node, int i) throws DOMException {
						Element photo = (Element) node;
						String photoUrl = "http://farm{farm-id}.static.flickr.com/{server-id}/{id}_{secret}_t.jpg";
						photoUrl = photoUrl.replaceAll("\\{farm-id\\}", photo.getAttribute("farm") );
						photoUrl = photoUrl.replaceAll("\\{server-id\\}", photo.getAttribute("server") );
						photoUrl = photoUrl.replaceAll("\\{id\\}", photo.getAttribute("id") );
						photoUrl = photoUrl.replaceAll("\\{secret\\}", photo.getAttribute("secret") );

						log.debug("THUMB URL:"+photoUrl);

						return photoUrl;
					}
				});
			} catch (Exception ex){
				log.warn("resttemplate failed but ignoring",ex);
			}
		}	
		log.debug("thumbnails found: ",thumbnails.size());
		return thumbnails;

	}


}

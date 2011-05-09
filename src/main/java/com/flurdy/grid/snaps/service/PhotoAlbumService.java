package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.*;
import com.flurdy.grid.snaps.exception.*;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.json.*;
import com.google.api.client.http.*;
import com.google.api.client.util.*;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AtomParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.JaxenXPathTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathOperations;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import java.io.IOException;
import java.util.*;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.GetMethod;


@Service
public class PhotoAlbumService extends AbstractService implements IPhotoAlbumService {

	@Autowired
	ITravellerService travellerService;

	@Resource
	protected Map<String, String> configurationMap;

	@Override
	public PhotoAlbum addAlbum(HolidayGroup holidayGroup, PhotoSharingProvider provider, String url) {

		final Traveller owner = travellerService.findCurrentTraveller();

		assert holidayGroup != null;
		assert provider != null;
		assert url != null;

		log.info("Traveller [" + owner.getSecurityDetail().getUsername()
				  + "] is adding album from provider [" + provider.name()
				  + "] to group [" + holidayGroup.getGroupName() + "] with url: " + url);


		if (holidayGroup.isMember(owner)) {

			if (provider.validUrl(url)) {

//				String providerId = provider.parseFlickrSetId(url);

//				log.debug("PROVIDER ID IS "+providerId);

				PhotoAlbum album = new PhotoAlbum.Builder()
						  .sharingProvider(provider)
						  .url(url)
//						.providerId(providerId)
						  .holidayGroup(holidayGroup)
						  .owner(owner)
						  .build();

				if (album.isValid()) {

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
	public PhotoAlbum findPhotoAlbum(long holidayId, long albumId) {

		assert holidayId > 0;
		assert albumId > 0;

		final HolidayGroup holidayGroup = holidayGroupRepository.findHolidayGroup(holidayId);
		if (holidayGroup != null) {
			final Traveller traveller = travellerService.findCurrentTraveller();
			if (holidayGroup.isMember(traveller)) {
				final PhotoAlbum photoAlbum = photoAlbumRepository.findAlbum(albumId);
				if (photoAlbum != null) {
					if (photoAlbum.getHolidayGroup().equals(holidayGroup)) {
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

		Collection<String> thumbnails = new HashSet<String>();

		if (photoAlbum.getSharingProvider().equals(PhotoSharingProvider.FLICKR)) {

			if (configurationMap.containsKey("flickrApiKey") && configurationMap.get("flickrApiKey") != null && configurationMap.get("flickrApiKey").trim().length() > 0) {

				final String photosetUrl = "http://www.flickr.com/services/rest/?method={method}" +
						  "&api_key={api_key}&photoset_id={photoset_id}&media=photos&per_page=3&format=rest&privacy_filter=public%20photos";
				Map<String, String> parameters = new HashMap<String, String>() {{
					put("method", "flickr.photosets.getPhotos");
					put("api_key", configurationMap.get("flickrApiKey"));
					put("photoset_id", photoAlbum.getSharingProvider().parseFlickrSetId(photoAlbum.getUrl()));
				}};

				log.debug("API:" + configurationMap.get("flickrApiKey"));
				log.debug("ALBUM URL:" + photoAlbum.getUrl());
				log.debug("ID:" + parameters.get("photoset_id"));

				final RestTemplate restTemplate = new RestTemplate();

				try {

					Source flickrResponse = restTemplate.getForObject(photosetUrl, Source.class, parameters);

					//				log.debug("flickr response?:"+flickrResponse);
					XPathOperations xpathTemplate = new Jaxp13XPathTemplate();
					thumbnails = xpathTemplate.evaluate("//photo", flickrResponse, new NodeMapper() {
						public Object mapNode(Node node, int i) throws DOMException {
							Element photo = (Element) node;
							String photoUrl = "http://farm{farm-id}.static.flickr.com/{server-id}/{id}_{secret}_t.jpg";
							photoUrl = photoUrl.replaceAll("\\{farm-id\\}", photo.getAttribute("farm"));
							photoUrl = photoUrl.replaceAll("\\{server-id\\}", photo.getAttribute("server"));
							photoUrl = photoUrl.replaceAll("\\{id\\}", photo.getAttribute("id"));
							photoUrl = photoUrl.replaceAll("\\{secret\\}", photo.getAttribute("secret"));

							log.debug("THUMB URL:" + photoUrl);

							return photoUrl;
						}
					});
				} catch (Exception ex) {
					log.warn("resttemplate failed but ignoring", ex);
				}
			}
		} else if (photoAlbum.getSharingProvider().equals(PhotoSharingProvider.PICASA)) {

            PicasaWebProvider picasaWebProvider = new PicasaWebProvider();

            thumbnails.addAll( picasaWebProvider.findThumbnails( photoAlbum ) );










/*
			log.debug("ALBUM URL:"+photoAlbum.getUrl());

			final String userID = photoAlbum.getSharingProvider().parsePicasaUsername(photoAlbum.getUrl());
			final String albumID = findPicasaAlbumId(photoAlbum);

			final String albumURL = "http://picasaweb.google.com/data/feed/api/user/{userID}/albumid/{albumID}";

			Map<String,String> parameters = new HashMap<String,String>(){{
					put("userID",  userID);
					put("albumID", albumID);
				}};

			log.debug("UID:"+parameters.get("userID"));
			log.debug("AID:"+parameters.get("albumID"));
*/

//			final RestTemplate restTemplate = new RestTemplate();

//				try {

//					String picasaResponse = restTemplate.getForObject( albumURL, String.class, parameters );

//					log.debug("response?:"+picasaResponse);
//					XPathOperations xpathTemplate = new Jaxp13XPathTemplate();
//					thumbnails = xpathTemplate.evaluate("//photo", picasaResponse, new NodeMapper(){
//						public Object mapNode(Node node, int i) throws DOMException {
//							Element photo = (Element) node;
//							String photoUrl = "http://farm{farm-id}.static.flickr.com/{server-id}/{id}_{secret}_t.jpg";
//
////							photoUrl = photoUrl.replaceAll("\\{farm-id\\}", photo.getAttribute("farm") );
//
//							log.debug("THUMB URL:"+photoUrl);
//
//							return photoUrl;
//						}
//					});
//				} catch (Exception ex){
//					log.warn("resttemplate failed but ignoring",ex);
//				}

			// http://picasaweb.google.com/flurdy/Industriagata?feat=directlink

		}
		log.debug("thumbnails found: ", thumbnails.size());
		return thumbnails;

	}






	private String findPicasaAlbumIdWithAtom(final PhotoAlbum photoAlbum) {
		final String userId = photoAlbum.getSharingProvider().parsePicasaUsername(photoAlbum.getUrl());

		HttpTransport transport2 = GoogleTransport.create();
		GoogleHeaders headers = (GoogleHeaders) transport2.defaultHeaders;
		headers.setApplicationName("Snaps/0.0.1");
		//transport2.defaultHeaders.put("GData-Version", "2");
//		transport2.addParser(new JsonCParser());
		headers.gdataVersion = "2";
		AtomParser parser = new AtomParser();
		parser.namespaceDictionary = new XmlNamespaceDictionary();
		Map<String, String> map = parser.namespaceDictionary.namespaceAliasToUriMap;
		map.put("", "http://www.w3.org/2005/Atom");
		map.put("atom", "http://www.w3.org/2005/Atom");
		map.put("exif", "http://schemas.google.com/photos/exif/2007");
		map.put("gd", "http://schemas.google.com/g/2005");
		map.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		map.put("georss", "http://www.georss.org/georss");
		map.put("gml", "http://www.opengis.net/gml");
		map.put("gphoto", "http://schemas.google.com/photos/2007");
		map.put("media", "http://search.yahoo.com/mrss/");
		map.put("openSearch", "http://a9.com/-/spec/opensearch/1.1/");
		map.put("xml", "http://www.w3.org/XML/1998/namespace");
		transport2.addParser(parser);
		HttpRequest request = transport2.buildGetRequest();
		String userAlbumsURL = "https://picasaweb.google.com/data/feed/api/user/" + userId;
		request.setUrl(userAlbumsURL);
		try {
			HttpResponse response = request.execute();

			log.debug("GDATA: " + response.parseAsString());

		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}


		return null;
	}


	/*
	private String findPicasaAlbumIdOld(final PhotoAlbum photoAlbum) {

		final String albumsURL = "http://picasaweb.google.com/data/feed/api/user/{userID}";
		final String userID = photoAlbum.getSharingProvider().parsePicasaUsername(photoAlbum.getUrl());

		Map<String, String> parameters = new HashMap<String, String>() {{
			put("userID", userID);
		}};

		log.debug("UID:" + parameters.get("userID"));

		final RestTemplate restTemplate = new RestTemplate();

		List<Map<String, String>> albumIds = null;
		try {
			log.debug("response:" + restTemplate.getForObject(albumsURL, String.class, parameters));
			log.debug("");
			log.debug("");
			Source picasaResponse = restTemplate.getForObject(albumsURL, Source.class, parameters);
//				XPathOperations xpathTemplate = new JaxenXPathTemplate();
			XPathOperations xpathTemplate = new Jaxp13XPathTemplate();
			albumIds = xpathTemplate.evaluate("//atom:feed/atom:entry", picasaResponse, new NodeMapper() {
				public Object mapNode(Node node, int i) throws DOMException {
					Element album = (Element) node;
					Map<String, String> albumId = new HashMap<String, String>();
					log.debug("NODE ALBUM");
					log.debug("NODE ALBUM URL:" + album.toString());
					// gphoto:id		// title
					return albumId;
////							photoUrl = photoUrl.replaceAll("\\{farm-id\\}", photo.getAttribute("farm") );
				}
			});
		} catch (Exception ex) {
			log.warn("resttemplate failed but ignoring", ex);
		}

		final String albumName = photoAlbum.getSharingProvider().parsePicasaAlbumname(photoAlbum.getUrl());
		log.debug("album name:" + albumName);
		if (albumIds != null) {
			for (Map<String, String> albumId : albumIds) {
				if (albumId.containsKey("ALBUM NAME") && albumId.get("ALBUM NAME").equals(albumName)) {
					return albumId.get("ALBUM ID");
				}
			}
		}

		return null;
	}
	*/

}

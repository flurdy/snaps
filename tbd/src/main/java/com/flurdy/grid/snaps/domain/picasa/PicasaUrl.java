package com.flurdy.grid.snaps.domain.picasa;

import com.google.api.client.util.Key;
import com.google.api.client.googleapis.GoogleUrl;

public class PicasaUrl extends GoogleUrl {

  public static final String ROOT_URL = "https://picasaweb.google.com/data/";

  @Key
  public String kinds;

  public PicasaUrl(String encodedUrl) {
    super(encodedUrl);
  }

  public static PicasaUrl relativeToRoot(String relativePath) {
    return new PicasaUrl(ROOT_URL + relativePath);
  }




}

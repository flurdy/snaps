package com.flurdy.grid.snaps.domain.picasa;

import com.google.api.client.util.Key;

import java.util.List;

public class AlbumEntry {

  @Key
  public Author author;

  @Key("openSearch:totalResults")
  public int totalResults;

  @Key("link")
  public List<GoogleLink> links;

  String getPostLink() {
    return GoogleLink.find(links, "http://schemas.google.com/g/2005#post");
  }
  @Key("entry")
  public List<PhotoEntry> photos;
}

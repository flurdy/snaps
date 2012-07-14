package com.flurdy.grid.snaps.domain.picasa;

import com.google.api.client.util.Key;

import java.util.List;

public class GoogleLink {

  @Key("@href")
  public String href;

  @Key("@rel")
  public String rel;

  public static String find(List<GoogleLink> links, String rel) {
    if (links != null) {
      for (GoogleLink link : links) {
        if (rel.equals(link.rel)) {
          return link.href;
        }
      }
    }
    return null;
  }
}

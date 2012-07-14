package com.flurdy.grid.snaps.domain.picasa;

import com.google.api.client.util.Key;

public class PhotoEntry {

  @Key
  public Category category = Category.newKind("photo");

  @Key("media:group")
  public MediaGroup mediaGroup;
}

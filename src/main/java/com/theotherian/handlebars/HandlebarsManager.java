package com.theotherian.handlebars;

import com.github.jknack.handlebars.Handlebars;

public final class HandlebarsManager {
  
  private static final HandlebarsManager INSTANCE = new HandlebarsManager();
  
  private final Handlebars handlebars;
  
  private HandlebarsManager() {
    Handlebars handlebars = new Handlebars();
    this.handlebars = handlebars;
  }
  
  public static Handlebars get() { return INSTANCE.handlebars; }

}

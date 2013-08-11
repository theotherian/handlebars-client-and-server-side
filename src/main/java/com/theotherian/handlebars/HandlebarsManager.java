package com.theotherian.handlebars;

import java.io.IOException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public final class HandlebarsManager {
  
  private static final HandlebarsManager INSTANCE = new HandlebarsManager();
  
  private final Handlebars handlebars;

  private HandlebarsManager() {
    Handlebars handlebars = new Handlebars();
    handlebars.registerHelper("latestmessages", new Helper<String>() {

      @Override
      public CharSequence apply(String context, Options options) throws IOException {
        return options.fn(MessagesDatastore.getMessagesByName(context));
      }
    });
    this.handlebars = handlebars;
  }
  
  public static Handlebars get() { return INSTANCE.handlebars; }

}

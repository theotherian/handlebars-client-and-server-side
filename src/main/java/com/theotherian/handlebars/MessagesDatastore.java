package com.theotherian.handlebars;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class MessagesDatastore {
  
  private Map<String, List<String>> messagesByName = Maps.newHashMap();
  
  private static final MessagesDatastore INSTANCE = new MessagesDatastore();
  
  private MessagesDatastore() {
    messagesByName.put("Ian", Lists.newArrayList("Going to lunch", "Wish I didn't eat that..."));
    messagesByName.put("Chris", Lists.newArrayList("Buying a new Apple product", "A bit poorer now"));
    messagesByName.put("Denny", Lists.newArrayList("My shoulder is feeling much better", "Well, I hurt my shoulder again"));
  }
  
  public static List<String> getMessagesByName(String name) {
    return INSTANCE.messagesByName.get(name);
  }

}

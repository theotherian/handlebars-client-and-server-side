package com.theotherian.handlebars;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class MessageDatastore {
  
  private Map<String, List<Message>> messagesByName = Maps.newHashMap();
  
  private static final MessageDatastore INSTANCE = new MessageDatastore();
  
  private MessageDatastore() {
    DateTime now = DateTime.now();
    messagesByName.put("Ian", Lists.newArrayList(
        Message.at("Wish I didn't eat that...", now.minusMinutes(5)),
        Message.at("Going to lunch", now.minusHours(1)))); 
    messagesByName.put("Chris", Lists.newArrayList(
        Message.at("A bit poorer now", now.minusHours(3)),
        Message.at("Buying a new Apple product", now.minusDays(1)))); 
    messagesByName.put("Denny", Lists.newArrayList(
        Message.at("Well, I hurt my shoulder again", now.minusDays(2)),
        Message.at("My shoulder is feeling much better", now.minusWeeks(1)))); 
  }
  
  public static List<Message> getMessagesByName(String name) {
    return INSTANCE.messagesByName.get(name);
  }

}

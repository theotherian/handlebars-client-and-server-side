package com.theotherian.handlebars;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class Message {
  
  public static Message at(String text, DateTime time) {
    Message message = new Message();
    message.text = text;
    message.when = new PrettyTime().format(time.toDate());
    return message;
  }
  
  private String text;
  
  public String getText() { return text; }
  public void setText(String text) { this.text = text; }
  
  private String when;
  
  public String getWhen() { return when; }
  public void setWhen(String when) { this.when = when; }
  
  
  @Override public boolean equals(Object obj) { return Pojomatic.equals(this, obj); }
  @Override public int hashCode() { return Pojomatic.hashCode(this); }
  @Override public String toString() { return Pojomatic.toString(this); }

}

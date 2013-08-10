package com.theotherian.handlebars;

import java.util.List;

import com.google.common.collect.Lists;

public class Args {
  
  public Args(String name, String... messages) {
    this.name = name;
    this.messages = Lists.newArrayList(messages);
  }
  
  private String name;
  
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  
  private List<String> messages;
  
  public List<String> getMessages() { return messages; }
  public void setMessages(List<String> messages) { this.messages = messages; }
}
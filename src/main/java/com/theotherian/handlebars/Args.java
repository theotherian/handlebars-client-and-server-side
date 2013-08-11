package com.theotherian.handlebars;

import java.util.List;

import com.google.common.collect.Lists;

public class Args {
  
  public Args(String... names) {
    this.names = Lists.newArrayList(names);
  }

  private List<String> names;
  
  public List<String> getNames() { return names; }
  public void setNames(List<String> names) { this.names = names; }
  
}
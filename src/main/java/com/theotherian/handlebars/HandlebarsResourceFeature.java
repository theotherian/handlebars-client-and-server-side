package com.theotherian.handlebars;

import javax.ws.rs.Path;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.apache.log4j.Logger;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;

public class HandlebarsResourceFeature implements DynamicFeature {

  private final Logger logger = Logger.getLogger(getClass());

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    if (Helper.class.isAssignableFrom(resourceInfo.getResourceClass())) {
      Class<?> resourceClass = resourceInfo.getResourceClass();
      String helperName = resourceClass.getAnnotation(Path.class).value();
      Handlebars handlebars = HandlebarsManager.get();
      if (handlebars.helper(helperName) == null) {
        Helper<?> helper = null;
        try {
          helper = resourceInfo.getResourceClass().asSubclass(Helper.class).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
          logger.fatal("Can't instantiate " + resourceClass.getCanonicalName(), e);
        }
        if (helper != null) {
          handlebars.registerHelper(helperName, helper);
        }
        else {
          logger.info("Not registering " + helperName + " a second time; already registered previously");
        }
      }
    }
  }

}

package com.github.marschall.jclequinox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * A {@link LogFactory} that delegates to {@link ExtendedLogService}.
 */
public class EquinoxLogFactory extends LogFactory {

  private final Map<String, Object> attributes;

  private final ConcurrentMap<String, Log> loggerMap;

  /**
   * Default constructor called by commons-logging.
   */
  public EquinoxLogFactory() {
    this.attributes = Collections.synchronizedMap(new HashMap<>());
    this.loggerMap = new ConcurrentHashMap<>();
  }
  
  private ExtendedLogService getLogService() {
    return EquinoxLogBundleActivator.getDefaultInstance().getExtendedLogService();
  }

  @Override
  public Log getInstance(Class clazz) {
    String className = clazz.getName();
    Log jclLog = this.loggerMap.get(className);
    if (jclLog == null) {
      Bundle bundle = FrameworkUtil.getBundle(clazz);
      Logger equinoxLogger = this.getLogService().getLogger(bundle, className);
      Log newLog = new EquinoxLog(equinoxLogger);
      Log previousLog = this.loggerMap.putIfAbsent(className, newLog);
      if (previousLog == null) {
        jclLog = newLog;
      } else {
        jclLog = previousLog;
      }

    }
    return jclLog;
  }

  @Override
  public Log getInstance(String name) {
    Log jclLog = this.loggerMap.get(name);
    if (jclLog == null) {
      Logger equinoxLogger = this.getLogService().getLogger(name);
      Log newLog = new EquinoxLog(equinoxLogger);
      Log previousLog = this.loggerMap.putIfAbsent(name, newLog);
      if (previousLog == null) {
        jclLog = newLog;
      } else {
        jclLog = previousLog;
      }

    }
    return jclLog;
  }

  @Override
  public void release() {
    this.loggerMap.clear();
  }

  @Override
  public Object getAttribute(String name) {
    return this.attributes.get(name);
  }

  @Override
  public String[] getAttributeNames() {
    return this.attributes.keySet().toArray(new String[0]);
  }

  @Override
  public void removeAttribute(String name) {
    this.attributes.remove(name);
  }

  @Override
  public void setAttribute(String name, Object value) {
    if (value != null) {
      this.attributes.put(name, value);
    } else {
      this.attributes.remove(name);
    }
  }

}

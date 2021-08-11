package com.github.marschall.jclequinox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A {@link LogFactory} that delegates to {@link ExtendedLogService}.
 */
public class EquinoxLogFactory extends LogFactory {

  private final Map<String, Object> attributes;

  private final ConcurrentMap<String, Log> loggerMap;

  private final ExtendedLogService logService;

  /**
   * Default constructor called by commons-logging.
   */
  public EquinoxLogFactory() {
    this.attributes = Collections.synchronizedMap(new HashMap<>());
    // this is a bit hairy
    // since we are fragment and not a bundle we can't have an activator we have to work around this

    Bundle bundle = FrameworkUtil.getBundle(EquinoxLogFactory.class);
    // start the bundle so that we have a bundle context
    // maybe the bundle is not started because it has no Bundle-ActivationPolicy: lazy
    if (bundle.getState() == Bundle.RESOLVED) {
      try {
        bundle.start();
      } catch (BundleException e) {
        throw new RuntimeException("could not start bundle", e);
      }
    }
    // reimplement BundleActivator#start()
    BundleContext context = bundle.getBundleContext();
    ServiceTracker<?, ExtendedLogService> serviceTracker =
        new ServiceTracker<>(context, ExtendedLogService.class, null);

    serviceTracker.open();
    // reimplement BundleActivator#stop()
    context.addBundleListener((BundleEvent event) -> {
      if ((event.getBundle().getBundleId() == bundle.getBundleId())
          && (event.getType() == BundleEvent.STOPPING)) {
        serviceTracker.close();
      }
    });


    this.logService = serviceTracker.getService();
    this.loggerMap = new ConcurrentHashMap<>();
  }

  @Override
  public Log getInstance(Class clazz) throws LogConfigurationException {
    String className = clazz.getName();
    Log jclLog = this.loggerMap.get(className);
    if (jclLog == null) {
      Bundle bundle = FrameworkUtil.getBundle(clazz);
      Logger equinoxLogger = this.logService.getLogger(bundle, className);
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
  public Log getInstance(String name) throws LogConfigurationException {
    Log jclLog = this.loggerMap.get(name);
    if (jclLog == null) {
      Logger equinoxLogger = this.logService.getLogger(name);
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

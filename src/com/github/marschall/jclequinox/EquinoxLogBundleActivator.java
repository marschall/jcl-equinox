package com.github.marschall.jclequinox;

import org.eclipse.equinox.log.ExtendedLogService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Bundle activator whose sole purpose is to give access to {@link BundleContext}.
 */
public final class EquinoxLogBundleActivator implements BundleActivator {

  private static volatile EquinoxLogBundleActivator defaultInstance;

  private volatile ServiceTracker<?, ExtendedLogService> serviceTracker;

  /**
   * Default constructor to be called by OSGi.
   */
  public EquinoxLogBundleActivator() {
    defaultInstance = this;
  }

  @Override
  public void start(BundleContext context) {
    this.serviceTracker = new ServiceTracker<>(context, ExtendedLogService.class, null);
    this.serviceTracker.open();
  }

  @Override
  public void stop(BundleContext context) {
    this.serviceTracker.close();
  }

  ExtendedLogService getExtendedLogService() {
    return this.serviceTracker.getService();
  }

  static EquinoxLogBundleActivator getDefaultInstance() {
    return defaultInstance;
  }

}

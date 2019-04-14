package com.github.marschall.jclequinox;

import org.apache.commons.logging.Log;
import org.eclipse.equinox.log.Logger;
import org.osgi.service.log.LogService;

/**
 * Implementation of {@link Log} that delegates to {@link Logger}.
 */
final class EquinoxLog implements Log {

  /**
   * Anything that is not one of LOG_DEBUG, LOG_ERROR, LOG_INFO, LOG_WARNING is trace.
   */
  private static final int LOG_TRACE = LogService.LOG_DEBUG + 1;

  private final Logger logger;

  EquinoxLog(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void trace(Object message) {
    if (this.logger.isTraceEnabled()) {
      this.logger.trace(message.toString());
    }
  }

  @Override
  public void trace(Object message, Throwable t) {
    if (this.logger.isDebugEnabled()) {
      this.logger.log(LOG_TRACE, message.toString(), t);
    }
  }

  @Override
  public void debug(Object message) {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug(message.toString());
    }
  }

  @Override
  public void debug(Object message, Throwable t) {
    if (this.logger.isDebugEnabled()) {
      this.logger.log(LogService.LOG_DEBUG, message.toString(), t);
    }
  }

  @Override
  public void info(Object message) {
    if (this.logger.isInfoEnabled()) {
      this.logger.info(message.toString());
    }
  }

  @Override
  public void info(Object message, Throwable t) {
    if (this.logger.isInfoEnabled()) {
      this.logger.info(message.toString());
    }
  }

  @Override
  public void warn(Object message) {
    if (this.logger.isWarnEnabled()) {
      this.logger.warn(message.toString());
    }
  }

  @Override
  public void warn(Object message, Throwable t) {
    if (this.logger.isDebugEnabled()) {
      this.logger.log(LogService.LOG_WARNING, message.toString(), t);
    }
  }

  @Override
  public void error(Object message) {
    if (this.logger.isErrorEnabled()) {
      this.logger.error(message.toString());
    }
  }

  @Override
  public void error(Object message, Throwable t) {
    if (this.logger.isErrorEnabled()) {
      this.logger.log(LogService.LOG_ERROR, message.toString(), t);
    }
  }

  @Override
  public void fatal(Object message) {
    if (this.logger.isErrorEnabled()) {
      this.logger.error(message.toString());
    }
  }

  @Override
  public void fatal(Object message, Throwable t) {
    if (this.logger.isErrorEnabled()) {
      this.logger.log(LogService.LOG_ERROR, message.toString(), t);
    }
  }

  @Override
  public boolean isDebugEnabled() {
    return this.logger.isDebugEnabled();
  }

  @Override
  public boolean isErrorEnabled() {
    return this.logger.isErrorEnabled();
  }

  @Override
  public boolean isFatalEnabled() {
    return this.logger.isErrorEnabled();
  }

  @Override
  public boolean isInfoEnabled() {
    return this.logger.isInfoEnabled();
  }

  @Override
  public boolean isTraceEnabled() {
    return this.logger.isDebugEnabled();
  }

  @Override
  public boolean isWarnEnabled() {
    return this.logger.isWarnEnabled();
  }

}

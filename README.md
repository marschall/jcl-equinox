JCL Equinox
===========
This is an implementation of [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) using the [Equinox](https://www.eclipse.org/equinox/) [ExtendedLogService](https://bugs.eclipse.org/bugs/show_bug.cgi?id=260672).


Implementation Notes
--------------------

The implementation is a fragment of the `org.apache.commons.logging` with a [commons-logging.properties](https://commons.apache.org/proper/commons-logging/guide.html) setting the implementation of the `LogFactory`.

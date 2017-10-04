# Drift Dummy

This is a very dumb and simple prototype of the upcoming Project Drift.

Implemented features:

 * Listen for Netflow 5 (UDP) packages on port `8877`
 * Forwards them to an elastic search instance started on `localhost`, port `9200`
 * Shows some visualizations with d3-sankey.

## Installation

### Build the project
`mvn clean install`

### Start elasticsearch
`$ELASTIC_HOME/bin/elasticsearch`

### Start the Drift Dummy
`java -jar target/drift-0.0.1-SNAPSHOT.jar`

### Send Netflow 5 packages to port 8877
e.g. with [Netflow-Generator](https://github.com/mshindo/NetFlow-Generator):

`flowgen -w 1000 -n 1000 -f 1 --srcaddr 10.0.0.1:3 --dstaddr 20.0.0.1:10  --dstport 22-80 -p 8877 localhost`

# Visualize it
Visualize it with your web browser of choice (Internet Explorer is not a choice)
http://localhost:8080/index4.html


## Some Notes

 Some notes to take in consideration when implementing Project Drift.

 * add "sender address" to the flow
 * add "location" to the flow
 * should the template be cached after restart of application or is in-memory sufficient
 * ensure to verify each received package
 * ensure to check for missing sequences (if possible)
 * how to deal with compression of data?!
 * sflow: which version should be supported (4 or 5)?
 * sflow: http://www.sflow.org/developers/specifications.php
 * jflow: is identical to netflow
 * jflow: which version should be supported (5, 8, 9) ?
 * v9 ensure that "sender" and "tempalte id" are cached
 * v9 where to get Length N from, e.g. IN_BYTES (default=4)
 * v9 have a look at options templates and options data records and implement accordingly
 * if elasticsearch is used, what should be the index and type?
 
 ## Libaries (Java)
 
 The following libraries have been found, but not evaluated.
 
  * sflow, ipfix: https://github.com/de-cix/jFlowLib
  * netflow 5,9 (no options) (Scala): https://github.com/wasted/netflow
  * jflow: https://github.com/aptivate/netgraph/tree/master/jflow-0.3
Zappos
======

This repo contains source of two applications
  1. UserInfo ( contains source for collecting user info for price alert 
  and writes it into a file. The 2nd application will read the info from
  this file and decide upon whether to alert user or not.)
  
  2. PriceAlerter ( responsible for checking all the user info saved into
  the file on a timely basis which can be set using the config.properties
  file. This actually is responsible for sending the mails to the users.
  This application should always be running. As of now it runs in an infinite
  loop.)
  
API used:
Search API was used. The user can enter either of Product Id / Name. So by 
using Search API we can know whether there exists a product with the details
given by the user and check for its percentOff option. 

Product API also could have been used but there is no way to get the product
details by using product name as an input. Product API currently supports only
retrival by product id only.

Suggestion:
1. If the Product API supports retrival by product name also, then using the
product name array (currently supports array of size 10 for product id's), number
of API requests can be reduced by 900%. In one call you can get details about
10 products. This can be done with existing API's, but only with product id and
not with product names. Since the user can enter either of Id or Name, using 
Search API was the better option.

  
  

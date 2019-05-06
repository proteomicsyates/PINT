# PINT
PINT is a new comprehensive web-based system to store, visualize, and query experimental proteomics data obtained under different experimental conditions and projects. PINT provides a very powerful query system through the use of different proteomics-specific data query commands running over both the experimental features and external annotations of the detected protein lists (such as UniprotKB).

## More information at the wiki
For more information about how to install PINT, how to configure it, how to submit data, and how to query datasets, go to the wiki page [https://github.com/proteomicsyates/PINT/wiki]  
  
## How to get PINT
You can download the latest version of the web application in a war file at: [http://pint.scripps.edu/]  
  
All source code is in this github project. 
It is divided in different modules, being [**pint_webapp**](https://github.com/proteomicsyates/PINT/tree/master/pint_webapp) the actual web application. All its dependencies can be retrieved by maven using the following Maven repositories:
  
**Maven repositories:**  
 ```
<repository>  
  <id>internal</id>  
  <url>http://sealion.scripps.edu/archiva/repository/internal/</url>  
</repository>  
<snapshotRepository>  
  <id>snapshots</id>  
  <url>http://sealion.scripps.edu/archiva/repository/snapshots/</url>  
</snapshotRepository>  
```

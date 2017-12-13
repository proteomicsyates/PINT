# PINT
PINT is a new comprehensive web-based system to store, visualize, and query experimental proteomics data obtained under different experimental conditions and projects. PINT provides a very powerful query system through the use of different proteomics-specific data query commands running over both the experimental features and external annotations of the detected protein lists (such as UniprotKB).

## How to get PINT
You can download the latest version of the web application in a war file at: [http://sealion.scripps.edu/PINT/]  
  
Alternatively, you can get it from our **maven repository**:  
  
**Dependency:**  
```
<dependency>  
  <groupId>edu.scripps.yates</groupId>  
  <artifactId>pint_webapp</artifactId>  
  <version>0.2.0-SNAPSHOT</version>  
</dependency>  
```  
  
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

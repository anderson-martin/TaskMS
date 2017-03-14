This is the backend for the TaskManager application. We did this as part of our school project. In Master branch, you can see 
version that we used for the application. However, for extra learning, I'm developing a parallel verison, which can be found in Martin branch. These two verisons are very different from each other.



## How to use the code in master branch

Import project to Intellij IDEA

1 download IDEA ultimate version
2 download glassfish server: 
https://drive.google.com/file/d/0B_Kren52OZsCS3g3ajlmV21xMXc/view?usp=sharing
Why: 
+ two packages in glassfish/modules has been changed to fix bugs
+ lastest glassfish version. Applcation fails to run on others

3 clone project to your workspace

4 import the project to Intellij IDEA 

4.1 open IDEA, choose `Import Project`
4.2 navigate to `taskms` folder, choose `OK`
4.3 `Import Project` is opened, IDEA automatically chose `Maven` in `Import project from external model` -> choose `Next` -> `Next` -> `Next` -> select SDK (1.8) and `Next` -> `Finish`

5 Choose sever to delpoy web service:

5.1 In menu bar, choose `Run` -> `Edit Configurations...`
5.2 Choose green `+` symbol on the upper left conner of the opened `Run/Debug Configurations` window. ->  select `GlassFish Server`-`Local`
5.3 `Application server:` lead to the downloaded server
5.4 `Server Domain:` : click down arrow button on the left, you should be able to select `domain1` from there
5.5 Click `Fix` for the warning at the bottom: `Warning: No artifacts marked for deployment`. -> `taskms:war exploded` -> `Apply` -> `OK` 

6 Configure database connection

6.1 Search for resource by Shift + Shift with keyword `persistence.xml`
6.2
For javax.persistence.jdbc.url replace `javahelps` by table name in your database, or you can create a database with the same name
For javax.persistence.jdbc.user/password use your credential to access database 
6.3 For hibernate.hbm2ddl.auto -> 
use `create-drop` hibernate will automattically generate database table (should be used for the first time this app run in your computer)
use `validate` hibernate will check the tables if exists, which save you from the time needed to create tables and populate them.

7 Run the application by clicking the green triangle button on the upper right corner of the view , or press Shift F10

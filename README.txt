1) JNDI configuration for DB connection

in TOMCAT_HOME\conf\context.xml add following elements within the <Context> element

<!-- Data Sources Definitions-->
<Resource name="jdbc/smartwg"
      auth="Container"
      type="javax.sql.DataSource"
      maxActive="100"
      maxIdle="30"
      maxWait="10000"
      username="<user>"
      password="<password>"
      driverClassName="com.mysql.jdbc.Driver"
      url="jdbc:mysql://localhost:3306/SmartWG"/>

<ResourceLink name="jdbc/smartwg"
      global="jdbc/smartwg"
      auth="Container"
      type="javax.sql.DataSource"/>
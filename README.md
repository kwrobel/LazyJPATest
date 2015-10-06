# LazyJPATest
Project for testing lazy reading of child and parent entities using Hibernate and OpenJPA providers

This project was created as a proof-of-concept to determine whether Hibernate and OpenJPA have a problem lazy-loading child entities or a parent entity. It includes both Hibernate 4.3.11 Final jars as well as OpenJPA 2.3.0 (all) jars. A test database is included with the project. It is the SAMPLE Derby database that comes with NetBeans / GlassFish. The database is packaged as a jar file and included on the classpath. The Derby 10.10.2.0 driver is also included and uses the embedded driver model.

The project includes two persistence units in the persistence.xml file: one for Hibernate and one for OpenJPA. To test either or, please have a look at /META-INF/config.properties. Replace the persistence unit with the one to be tested against.

Finally, since this is a proof-of-concept, the result for both Hibernate and OpenJPA is that lazy loading actually works.

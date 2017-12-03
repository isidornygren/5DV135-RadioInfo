# RadioInfo
RadioInfo skapades för kursen Applikationsutveckling i Java som går under hösttermingen 2017 på Umeå Universitet.

![Image of the application](/application.png)

[Laborationsspecifikationen](https://git.cs.umu.se/computingscience/5dv135-apjava-ht17/tree/master/assignments/2)

[Kursinformation](http://www8.cs.umu.se/kurser/cambrogateway/?id=57267HT17-1)
## Beskrivning
RadioInfo hämtar data från SRs [API](http://sverigesradio.se/api/documentation/v2/index.html) för deras radiokanaler, 
parsar datan som tas emot och bygger upp ett system av avsnitt i en tablå samt kanaler.
Dessa presenteras visuellt med hjälp av Swing biblioteket. 
## Kompilering
För att kompilera applikationen används Maven. För att skapa en körbar .jar fil 
genom att köra `mvn package` i samma mapp som `pom.xml` filen ligger i, dvs RadioInfo.
Detta generar den körbara .jar-filen i `target` mappen.
## Körning
Den kompilerade .jar-filen kommer ligga i `target` mappen efter kompilering, och
bör heta `RadioInfo-(version)-jar-with-dependencies.jar`. 
För att exekvera programmet körs `java -jar target/RadioInfo-1.0-jar-with-dependencies.jar`
## Tester
Tester körs automatiskt tillsammans med kompileringen under exekveringen
av `mvn package`, för att enbart köra tester körs `mvn test` i samma mapp
som `pom.xml`, nedan följer hur ett positivt testresultat bör se ut.
```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running RadioInfo.model.ChannelParserTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.142 sec
Running RadioInfo.model.ChannelTest
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.938 sec
Running RadioInfo.model.EpisodeTest
Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.058 sec
Running RadioInfo.model.ParserTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 sec
Running RadioInfo.model.ParsingErrorTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 sec
Running RadioInfo.model.ScheduleParserTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.097 sec

Results :

Tests run: 43, Failures: 0, Errors: 0, Skipped: 0

[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ RadioInfo ---
[INFO] Building jar: /Users/isidornygren/Documents/Skola/5DV135/5DV135-RadioInfo/target/RadioInfo-1.0.jar
[INFO] 
[INFO] --- maven-assembly-plugin:3.1.0:single (make-assembly) @ RadioInfo ---
[INFO] Building jar: /Users/isidornygren/Documents/Skola/5DV135/5DV135-RadioInfo/target/RadioInfo-1.0-jar-with-dependencies.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.276 s
[INFO] Finished at: 2017-12-03T12:03:30+01:00
[INFO] Final Memory: 17M/273M
[INFO] ------------------------------------------------------------------------
```

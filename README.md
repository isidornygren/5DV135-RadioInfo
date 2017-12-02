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
För att kompilera applikationen används Maven. För att skapa en körbar .jar fil så
kör `X` i samma mapp som `pom.xml` filen ligger i, dvs RadioInfo.
## Körning
Den kompilerade .jar-filen kommer ligga i `target` mappen efter kompilering. För att exekvera programmet körs 
`java -jar target/RadioInfo-1.0.jar`
## Tester
Kommer snart

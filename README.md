# Beleg verteilte Systeme

Gruppen Mitglieder
- s86306 Marius Thomas Hoffmann (Ansprechpartner)
- s86431 Frederic Niklas Egle
- s87127 Til Guhlmann
- s86379 Niklas Benjamin Große

## Verwendete Architektur
Es wurde eine Model-View-Controller (MVP)-Architektur verwendet.
Ausführende Systeme können die folgenden zwei Aufgabentypen einnehmen: Master und Worker.
Der Master dient der Koordination der zu berechnenden Aufgabe, während der Worker die zu berechnende Aufgabe berechnet.
Insofern keine Worker registriert sind, übernimmt der Master auch eine Worker-Rolle. 
Eine Ein-System-Architektur, bei der ein System sowohl Master als auch Worker darstellt, sieht wie folgt aus:

![Lokale Architektur](/images/local_arch.png)

Sobald sich Worker auf dem Master registrieren, berechnet der Master selbst nicht mehr und delegiert nur an die Worker. 
Eine n-System-Architektur mit einem Master und n Workern sieht dann wie folgt aus:

![Verteilte Architektur](/images/dist_arch.png)

## Leistungsgewinn

![Zeitmessungen](/images/time_measurement.png)

Im Diagramm sind die Zeitmessungen für den vorgegeben Zoompunkt mit 100 Zoomstufen und max. 1000 Iterationen dargestellt.
Hierbei ist auf der Abszisse die Anzahl der Worker und auf der Ordinate die benötigte Zeit in Millisekunden abgetragen, jeweils für 1, 4 bzw. 8 Threads pro Worker.
Es ist abzulesen, dass die benötigte Zeit mit steigender Anzahl an Threads sinkt, da ein System parallel rechnet.
Für einen Thread ist zu erkennen, dass mit steigender Anzahl an Workern auch die benötigte Zeit geringer wird, da mehrere Systeme hierbei parallel berechnen.
Ebenfalls ist zu sehen, dass ein Remote Worker etwas langsamer ist als die lokale Berechnung, was zu erwarten ist, da durch den Remoteaufruf ein zusätzlicher Overhead entsteht.
Insgesamt ist bei einem Thread pro Worker der Leistungsgewinn am besten zu erkennen, bei acht Threads hingegen sieht man sogar eine Verschlechterung in der benötigten Berechnungszeit.
Dies kann an der Implementation liegen, da bereits bei einer lokalen Berechnung aufgefallen ist, dass durch die immer wieder benötigte Berechnung der vermischten Farben viele Objekte im Speicher allokiert werden.
Für die lokale Berechnung wurde dieses Problem gelöst, indem beim Setzen der maximalen Iterationen des Models die Farben berechnet und gespeichert werden, wodurch die vermischten Farben nur einmal, und nicht pro Pixel berechnet werden müssen.



Die verteilte Implementation erzeugt jedoch bei jeder Instantiierung einer Worker-Aufgabe ein neues Model und setzt dessen maximale Iterationen, wodurch hier wieder jeweils teuer die vermischten Farben berechnet und allokiert werden müssen.
Das Problem könnte behoben werden, indem die Implementation so angepasst wird, dass das Model mit den berechneten und allokierten vermischten Farben nur noch auf dem Master existiert und die Worker sich nur mit der Berechnung der Worker-Aufgaben beschäftigen, für die sie selbst die definierte Anzahl an Threads starten.
Des Weiteren ist bei der Verwendung von acht Threads auffällig, dass je nach Worker die Ausführungszeit bedeutend unterschiedlich ist.
Wenn der erste Worker auf dem gleichen System (Windows-System mit AMD Ryzen 5 5600X (6 Kerne)) wie der Master gestartet wird, beträgt die Berechnungszeit 23000 ms.
Wurde der erste Worker jedoch auf einem anderen System gestartet (hier wurde sowohl ein Windows-System mit AMD Ryzen 9 5900X (12 Kerne) als auch ein Apple Mac Mini mit M4-Prozessor (10 Kerne) getestet), so erhöhte sich die benötigte Ausführungszeit auf etwas über 50000 ms.
Der genaue Grund für die Performanz-Unterschiede ist unklar.
Eine Möglichkeit könnte sein, dass die Java Virtual Machine (JVM), in der alle Java-Programme laufen, diverse Caching-Mechanismen besitzt, die Prozess-übergreifend Objekte wiederverwendet, sodass der Worker schneller auf diese zugreifen kann, obwohl die Erwartung wäre, dass der Garbage Collector der JVM nicht-benutzte Objekte zeitnah de-allokieren sollte.


## Leistung der einzelnen Teammitglieder
Marius Hoffmann:
- Berechnung innerhalb des Models
- Farbverlauf (erste Variante)
- Verteilung der Berechnung auf Threads
- Dokumentation

Frederic Egle:
- erste Darstellungsform
- verbesserter Farbverlauf
- Bugfixes
- GUI (Zoom etc.)
- Dokumentation
- Geschwindigkeitstest

Til Guhlmann:
- Berechnung innerhalb des Models
- Farbverlauf (erste Variante)

Niklas Große:
- RMI-Implementation



# Distributed Chaos



## Opis Bootstrapa

Servis koji radi na fiksnom portu i definise se u konfiguracionom fajlu. Dodeljuje id odredjenom čvoru tako sto mu prosledjuje naredni slobodni id. ID čvora je u formatu IP:port.
Bootstrap sadrži:


Lista aktivnih čvorova


## Bootstrap API

- GET /api/bootstrap/hail 

Pri ukljucenju prvog čvora, port i ip adresa čvoru kome treba da se javi će biti null. U suprotnom, uzima random čvorova iz liste aktivnih čvorova i vraća je čvoru.

Bootstrap mu vraća čvor kome treba da se javi:
```
{
	“ip”: “<IP adresa čvora kome treba da se javi za uključenje u mrežu>”,
	“port”: “<Port čvora kome treba da se javi za uključenje u mrežu>”
}
```

- POST /api/bootstrap/left

Pri uklanjanju noda iz mreze, potrebno je javiti bootstrapu da vise ne nudi taj cvor kao opciju za uklucenje. Bootstap uklanja ovaj čvor iz liste aktivnih čvorova.  


Javljanje bootstrap serveru radi iskljucenja  mrezu:

```
{
	“ip”: “<Adresa čvora koji salje zahtev>”,
	“port”: “<Port čvora koji šalje zahtev>”
}
```



- PUT /api/bootstrap/new

Kada čvor uspe da se uključi u sistem javlja bootstapu da ga doda u listu aktivnih čvorova.

```
{
	“ip”: “<Adresa čvora koji se uključio>”,
	“port”: “<Port čvora koji se uključio>”
}
```


## Opis čvora

Da bi se čvor uključio u sistem mora bootstrap serveru da se javi na api/hail. Svaki čvor u sistemu ce imati informacije o svim čorovima u sistemu. Čvor ce na osnovu tih podataka kreirati i održavati svoju tabelu sledbenika. Kada se ukljuci u sistem (dobije id), prvo salje poruku ALL_NODES cvoru kome treba da se javi, nakon toga kreira svoju tabelu sledbenika, pa broadcastuje UPDATE poruku svom naredom cvoru (iz tabele sledbenika) i tako ostali čvorovi uključuju novi čvor u sistem.

Svaki čvorr koristi svog sledbenik i pretkodnika za cuvanje backup-a. Backup se radi na svakih 5 sekundi.

Karakteristike čvora:
IP Adresa na kojoj radi
Port na kome radi
ID u okviru sistema
Lista informacija o svim čvorovima
Tabela sladbenika
Referenca na prethodnika
Job ID
Region ID
	



## Čvor API

- GET /api/node/info


Osnovne informacije o čvoru.

```
{
  "ip": "<IP čvora>",
  "port": "<port čvora>"
}
```



- GET /api/node/allNodes 

Kada se novi čvor uključuje u sistem, traži od čvora kome se javio listu svih čvorova za koje on zna.

```
[
    {
        "ip": "localhost",
        "port": 1000
    },
    {
        "ip": "localhost",
        "port": 1001
    }
]
```




- GET /api/node/ping/{nodeID}

Ukoliko neko zeli da pinguje cvor u sistemu, to radi tako sto poziva ovaj endpoint, nodeID je ID cvora koji zelimo da pingujemo. Ukoliko cvor koji primi poruku nema trazeni ID, poruka se prosledjuje do tog ID-a u suportnom vraca se odgovarajuci odgovor.


Čvor nam odgovara da je živ, odnsno vraca true ili false

- POST /api/node/quit

Uredno gašenje čvora. Možemo samo da ga ugasimo i pustimo sistem da shvati da je mrtav, ili možemo svom prvom sledbeniku da pošaljemo poruku da se gasimo kako bi odmah mogao da pokrene rekunstrukciju sistema, bez čekanja 5000ms.
Koristeći  /api/node/left/{nodeID} obaveštavamo suseda da izlazimo iz sistema.


- GET /api/node/left/{nodeID}

Ovde prima poruku da cvor sa nodeID-em napusta sistem, svojom voljom ili otkazom. Cvor je u obavezi da obavesti sledbenika da je cvor sa ID-em nodeID napustio sistem, tako sto salje GET zahtev na /api/node/left/{nodeID} svom sledbeniku. Nakon toga salje bootstrap-u POST zahtev na /api/bootstrap/left kako bi ga bootstrap izbacio iz svoje liste. Kao request body treba proslediti ip i port koji se dobija iz nodeID-a. Posto je cvor sa id-em nodeID napustio sistem, opet treba da odradi raspodelu poslova tako da izostavi izbacen cvor. 


- GET  /api/node/new/{nodeID}

Prima poruke za ukljucenje novog cvora u sistem. Poruka se treba proslediti predhodniku kako bi svi cvorovi saznali za novi cvor i reorganizovali posao. Novi cvor se treba dodati u kolekciju svih cvorova, nakon cega ponovo raspodeljuje posao, tako da uracuna i novi cvor. Koristi se GET  /api/node/new/{nodeID} za slanje poruke sledbeniku.


- POST /api/node/backup


Prima backup za odredjeni jobID i regionID.

Skup tačaka koje treba sacuvati kao backup

```
{
   	“jobID”: “<jobID>”
	“regionID”: “<regionID>”
	"data": [
        {
            "x": <x>,
            "y": <y>
        },
        {
            "x": <x>,
            "y": <y>
        }
    ]
}
```




- GET api/node/backup/{jobID}/{regionID}

Vraća sacuvane podatke za trazeni jobID i regionID

```
{
    "jobID": "<jobID>",
    "regionID": "<regionID>",
    "data": [
        {
            "x": <x>,
            "y": <y>
        },
        {
            "x": <x>,
            "y": <y>
        }
    ]
}
```



- GET /api/node/allJobs

Vraća sve poslove u sistemu
Posao API

- GET api/jobs/status
Prikazujemo stanje svih započetih izračunavanja na nivou celog sistema. Broj tačaka na svakom fraktalu, koliko čvorova radi na svakom fraktalu, fraktalni ID i koliko tačaka je svaki čvor nacrtao. Poruka se broadcastuje kroz mrežu i svi čvorovi upisuju svoje rezultate. Čvor koji je dobio zahtev sve upakuje i vraća.

```
{
 "jobs": [ {
	"jobID": "<ID posla>",
	"regions": [
	  { 	"regionID": "<ID regiona>",
		"nodeID": "<ID cvora koji radi ovaj region>",
		"numberOfPoints":  “<koliko je tacaka nacrtao ovaj cvor>” },
 ...]
  }, ...]
}
```




- GET api/jobs/status/{jobID} 

Prikazujemo stanje svih započetih izračunavanja na nivou celog sistema za posao sa jobID-em. Koliko čvorova radi na poslu, koliko tačaka je koji čvor nacrtao, ID svakog regiona ovog posla. Čvor koji je dobio zahtev na osnovu jobID-a i liste svih čvorova zaključuje koji čvorovo rade ovaj posao i po Chordu njima šalje zahtev. Njihove rezultate sumira i vraća.

```
[
	  { 	"regionID": "<ID regiona>",
		"nodeID": "<ID cvora koji radi ovaj region>",
		"numberOfPoints":  “<koliko je tacaka nacrtao ovaj cvor>” 
}, {}, ...
]
``` 



- GET api/jobs/status/{jobID}/{regionID} 

Prikazujemo stanje svih započetih izračunavanja za posao sa jobID na regionu regionID. ID regiona, ID čvora koji ga izvršava i broj tačaka koje su trenutno izračunate. Čvor koji je dobio zahtev po jobID-u i regionID-u zaključuje koji je čvor odgovoran za ovaj region i po Chordu mu šalje zahtev.

```
{
	 "regionID": "<ID regiona>",
	"nodeID": "<ID cvora koji radi ovaj region>",
	"numberOfPoints":  “<koliko je tacaka nacrtao ovaj cvor>” 
}
```
	


- PUT /api/jobs/start 

Javljamo čvoru da započinje izvršavanje posla. Čvor ubacuje posao u listu poslova i broadcastuje poruku kroz sistem. Svaki čvor je dužan da se reorganizuje na osnovu broja čvorova i količine posla. Svaki posao se može podeliti na onoliko regiona (čvorova) koliko ima početnih tačaka.

```
{
  “jobiID”: “<ID posla koji se zapocinje>”,
  “startingPoints”: “[ {“x”:”1.3”, “y”: “-0.6”}, 
        {“x”:”1.3”, “y”: “-0.6”}, ...]”
}
```

	
- GET /api/jobs/result/{jobID}

Prikazujemo rezultat izračunavanja za posao jobID sa celog sistema. Ukoliko smo radili neki deo tog posla zabeležimo ga. Šaljemo poruku po Chordu cvoru koji je zaduzen za ta posao i njegovim backupovima, koristeći /api/{nodeID}/jobs/{jobID} kako bi pokupili sve delove posla jobID. Sve rezultate zapakujemo i vratimo.
	
```
{
  “jobID“: “<ID posla koji smo zatrazili>“,
  “startingPoints”: ”<lista početnih tačaka fraktala>”,
  “regions”: ”[ {“nodeID”: “<ID cvora koji je radio ovaj deo posla>”, 
        “points”: “[{“x”:”1.3”, “y”: “-0.6”}, 
{“x”:”6.6”, “y”: “1.9”}, 
...]”}, 
		                    {“nodeID”: “<ID cvora koji je radio ovaj deo posla>”, 
        “points”: “[{“x”:”10.7”, “y”: “-6.8”}, 
{“x”:”2.6”, “y”: “-11.4”}, 
...]”}]”,
“width”: “<širina bounding box-a>”,
“height”: “<visina bounding box-a>”
}
```


- GET /api/jobs/result/{jobID}/{regionID}

Prikazujemo rezultat izračunavanja za posao jobID za region regionID. Ukoliko smo mi radili deo posla jobID proveravamo da li smo bili zaduženi za region regionID. Ako jesmo vraćamo rezultat.
U suprotnom po chordu treba da pronađemo čvor koji je bio zadužen za taj region i on nam vraća rezultat.
Alternativa je da radimo isto kao za /api/jobs/result/{jobID} samo što na nivou čvora imamo dodatnu proveru za region.

```
{
  “jobID“: “<ID posla koji smo zatrazili>“,
  “nodeID”: “<ID cvora koji je radio ovaj deo posla>”,
  “regionID”: “<ID cvora koji je radio ovaj deo posla>”, 
  “startingPoints”: ”<lista početnih tačaka fraktala>”,
  “points”: “[{“x”:”1.3”, “y”: “-0.6”}, 
                                           {“x”:”6.6”, “y”: “1.9”}, ...]”
  “width”: “<širina bounding box-a>”,
  “height”: “<visina bounding box-a>”
}
```


- POST /api/jobs/stopAll/{jobID} 

Zaustavljamo izračunavanje za posao jobID na celoj mreži. Fraktal u potpunosti nestaje iz sistema i čvorovi se raspoređuju na druge poslove. Čvor briše posao jobID sa sebe i šalje poruku za brisanje kroz mrežu po chordu koristeći /api/jobs/stopOne/{jobID}
{}


- DELETE /api/jobs/{jobID} 

Uklanja posao sa id-em jobID iz kolekcije poslova, ponovo rasporedi posao za sebe, uklanja sve podatke o poslu sa id-em iz njegove memorije. Ukoliko je imao taj posao u kolekciji poslova, onda salje svom prvom sledbeniku, u suprotnom ne salje poruku jer to znaci da je on vec uklonio taj job i da nece da kreira beskonacni broadcast.


{}


- GET /api/delegate/{nodeID}/jobs/{jobID}

Vraćamo naš rezultat za jobID ukoliko smo radili taj posao ili backup tog posla ako ga čuvamo. Prosleđujemo poruku.

```
[ 
     {
	“x”:”1.3”,
“y”: “-0.6”
     }, 
     {
            “x”:”6.6”,
            “y”: “1.9”
     }
]
```



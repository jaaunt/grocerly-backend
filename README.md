# ITI0302-2025-backend

# 1. Projekti kirjeldus / eesmark

Tegemist on klassikalise toidu tellimus teenusega. Leht voimaldab klientidel valida tooteid ja lisada neid oma ostukorvi et siis neid koju tellida. Tegemist pole valmis soogi tellimis lehega vaid poest toodete tellimisega.(nagu barbora)

Leht on loodud aine ITI0302 raames aastal 2025 Taltechis. Tegemist pole paris tellimis lehega, turvalisuse huvides palume paris isikuandmeid lehele mitte sisestada.

# 2. Kuidas runnida - *server*
selle jaoks on eeldus et sul on serveriga tootav ssh voti voi keegi paneb sinu eest serveri runnima

### 2.1 Serveri kaivitamine

Runni windows powershellis voi mingis muus terminalis kasku ssh -i ubuntu@193.40.157.76.

Kui see kohe ei toota voib vaja minna et viitaksid oma ssh votmele ssh -i ~/.ssh/"sinu_ssh_votme_nimi" ubuntu@193.40.157.76.

Kui sa ei saa oma ssh votmega nii ligipaasu siis arvatavasti pole su ssh votit serverisse pannud. Vota uhendust kellegiga kes saaks sinu votit kas sinna lisada voi sinu eest seda runnida.

Kui so oled too lopetanud siis serverist saab valja kasuga exit.

### 2.2 Kuidas backendi serveris runnida

Runni nuud serveri sees kasku java -jar ~/backend/grocerly-0.0.1-SNAPSHOT.jar

See runnib backendi jar faili. Oota kuni see lopetab enne kui jatkad. Ara akent ega protsessi lopeta enne kui oled veebilehega too lopetanud.

Backendi processi saab lopetada vajutades ctrl + c

### 2.3 Mine lingile

Pane oma lemmik browseri search bari http://193.40.157.76

Voi vajuta ulal olevale lingile

### 2.4 Additional notes

Hetkel on products anmebaas tuhi (meil tekkis probleem localist andemte andmebaasi lisamisega) seega avaleht on hetkeseisuga tuhi kahjuks. Sellega tegeletakse. Userite tegemine ja login on korras.


# 3. Kuidas runnida - *local*

Koigepealt runni back siis front nagu ikka. Alusta kloonimisest.

- backend: https://gitlab.cs.taltech.ee/jaaunt/iti0302-2025-backend
- frontend: https://gitlab.cs.taltech.ee/jaaunt/iti0302-2025-frontend

### 3.0 Klooni molemad repod

Kui see pakub sulle backendi juures load gradle script parast kloonimist tee seda

## 3.1 Backend osa:

### 3.1.1 Runni dokkerit

Tee lahti oma arvutis dokker desktop

Tee oma IDE TERMINALIS docker-compose up -d

Andmebaasiga uhendamiseks kui sa varem pole on vaja votta paremalt datavase vaatest(kolme kihiline silindri nupp) pluss. Vali sealt alt postgrSQL seal on sul vaja userit ja passwordi need leiad sa docker-compose.yml failist.

Dockeri saab kinni panna kasuga IDE TERMINALIS docker compose down

### 3.1.2 Buildimine

Nuud peaks projekti gradleis igaksjuhuks ule buildima. Gradel nupp aseub paremal(elevant). Siis gradle menust grocerly -> Tasks -> build -> build.

### 3.1.3 Runni applicationit

Runni grocerly applicationi selle leiad siit alt -> iti0302-2025-backend -> grocerly -> src -> main -> java -> com.stitch.grocerly -> GrocerlyApplication.java runni kas seda faili voi funktsiooni selle faili sees.

## 3.2 Frontend osa:

### 3.2.1 Path

- Koigepealt vota grocerly path selles projektis. Mul oli see nt C:\Users\abc\IdeaProjects\iti0302-2025-frontend\grocerly sul peaks olema midagi sarnast.

- Pane see path intellij terminali nii: cd sinu path

- mul oli see naiteks nii: cd C:\Users\abc\IdeaProjects\iti0302-2025-frontend\grocerly

### 3.2.2 Installid

- Pane terminali nuud: npm install axios

- Pane terminali nuud: npm install pinia

- Pane terminali nuud: npm install

### 3.2.3 Runni

- Pane terminali nuud: npm run dev

### 3.2.4 Browser

- Ava oma lemmik browseris jargi oma sudant mis on oige valik: http://localhost:5173/

Eks nad koik vast tootavad.

### 3.2.5 Additional notes

Arvatavasti markad et esilehel pole uhtegi toodet hetkel see on selleparast et localis tehes pole andmebaasis sisu.

Seega on all users, orders, brands ja product tabelid tuhjad. Producte ja brande peab lisama intellij backendist andmebaasi ise juurde lisama. Usereid saad juurde lisada registreerimis viewst. See lisab userid ise andmebaasi. Ordereid saab samamoodi ise teha ostukorvi vaatest.

# 4. Kui sa tahad omale buildida selle backendi jar faili

Seda saab teha gradle aknast grocerly -> tasks -> build -> bootJar

# 5. Tehnoloogiad

- Spring boot
- Vue
- PostgreSQL

# 6. Prerequisites

- Java 21
- Node.js 18+ and npm
- PostgreSQL 16
- Docker Desktop (optional, for containerized setup)
- Git

# 7. Autorid

Janne-Lii Aun-Trepp ja Kristin Vares
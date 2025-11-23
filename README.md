# ITI0302-2025-backend

# Projekti kirjeldus / eesmark

Tegemist on klassikalise toidu tellimus teenusega. Leht voimaldab klientidel valida tooteid ja lisada neid oma ostukorvi et siis neid koju tellida. Tegemist pole valmis soogi tellimis lehega vaid poest toodete tellimisega.(nagu barbora)

## Kuidas runnida

Koigepealt runni back siis front nagu ikka.

- backend: https://gitlab.cs.taltech.ee/jaaunt/iti0302-2025-backend
- frontend: https://gitlab.cs.taltech.ee/jaaunt/iti0302-2025-frontend


## Backend osa:

### 1. Runni dokkerit

Tee lahti oma arvutis dokker desktop(hetkel pole repo sees uhendatud veel)

docker-compose up -d terminalis oma ides

Andmebaasiga uhendamiseks voi kui muul ajal kusib useri ja passwordi siis need leiab docker-compose.yml failis asukoht ITI0302-2025-BACKEND -> docker-compose.yml
Andmebaasiga uhendamiseks on paremal pool database nupp seda vajutades tuleb lahti aken vajuta + marki sealt alt leia postgres ja logi sisse. Kasutaja ja parooli leiad docker-compose.yml seest.

### 2. Buildimine

Projekti avamisel peab esmalt gradel buildima. Gradel nupp paremal. Siis gradle menust grocerly -> Tasks -> build -> build. 

### 3.Runni applicationit

Runni grocerly applicationi selle leiad siit alt -> iti0302-2025-backend -> grocerly -> src -> main -> java -> com.stitch.grocerly -> GrocerlyApplication.java seest.
Kui see ei ole runnable openi grocerly moodul teises aknas ja runnis seal seda. grocerly -> src -> main -> java -> com.stitch.grocerly -> GrocerlyApplication.java seest.

## Frontend osa:

### 1. Path

- Koigepealt vota grocerly path selles projektis. Mul oli see nt C:\Users\abc\IdeaProjects\iti0302-2025-frontend\grocerly sul peaks olema midagi sarnast.

- Pane see path intellij terminali nii: cd sinu path

- mul oli see naiteks nii: cd C:\Users\abc\IdeaProjects\iti0302-2025-frontend\grocerly

### 2. Installid

- Pane terminali nuud: npm install axios

 OOTA KUNI SEE LOPETAB KIIRET POLE KUHUGI, SEDA ON VAJA

- Pane terminali nuud: npm install

 OOTA KUNI SEE LOPETAB KIIRET POLE KUHUGI, SEDA ON VAJA

### 3. Runni

- Pane terminali nuud: npm run dev

 OOTA KUNI SEE LOPETAB KIIRET POLE KUHUGI, SEDA ON VAJA

### 4. Browser

- Ava oma lemmik browseris jargi oma sudant mis on oige valik: http://localhost:5173/

Eks nad koik vast tootavad.

# Tehnoloogiad

- Spring boot
- Vue
- PostgreSQL

# Additional notes

Andmebaas on algul tuhi mistottu on all users, orders, brands ja product tabelid tuhjad. Producte ja brande peab lisama intellij backendist andmebaasi ise juurde lisama. Usereid saad juurde lisada registreerimis viewst. See lisab userid ise andmebaasi. Ordereid saab samamoodi ise teha ostukorvi vaatest.
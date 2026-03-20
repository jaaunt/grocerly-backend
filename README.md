# Grocerly

## About this repository
This project was originally developed on GitLab for the ITI0302(2025) course at TalTech. The original version was deployed on a university server, however the server is not currently available(as of 20.03.2026) we will update the README once it becomes available, but you are welcome to check for yourself by clicking the link http://193.40.157.76 . The project was made as a group project by Janne-Lii Aun-Trepp ([jaaunt](https://github.com/jaaunt)) and Kristin Vares ([kristinvares](https://github.com/kristinvares)).

You can find the original Estonian README [here](https://github.com/jaaunt/grocerly-backend/blob/main/README_estonian.md).

## Project description

The website allows customers to browse products, add them to a cart and place orders for home delivery. Inpired by Barbora. 

This is not a real ordering service. Please do not enter any real personal information.

The project was originally developed on GitLab and was previously deployed on a university server.

## How to run locally

Clone both repos and run the backend first, then the frontend.
- [Backend](https://github.com/jaaunt/grocerly-backend)
- [Frontend](https://github.com/jaaunt/grocerly-frontend)

### Backend

#### 1. Start Docker
- Open Docker Desktop on your machine
- Run the following in your IDE terminal:
```bash
docker-compose up -d
```
- To connect to the database, open the Database view in your IDE (the cylinder icon on the right), click the + button and select PostgreSQL. The username and password can be found in `docker-compose.yml`
- To stop Docker run:
```bash
docker-compose down
```

#### 2. Build the project
Open the Gradle panel on the right (elephant icon) and run:
`grocerly → Tasks → build → build`

#### 3. Run the application
Run the main application file found at:
`grocerly → src → main → java → com.stitch.grocerly → GrocerlyApplication.java`

#### 4. Build a JAR file (optional)
`grocerly → Tasks → build → bootJar`

---

### Frontend

#### 1. Navigate to the frontend folder
```bash
cd path/to/iti0302-2025-frontend/grocerly
```

#### 2. Install dependencies
```bash
npm install axios
npm install pinia
npm install
```

#### 3. Run
```bash
npm run dev
```

#### 4. Open in browser
```
http://localhost:5173/
```

#### Note
On first run the product list will be empty since the local database has no content. Products and brands must be added manually through the backend. Users can be created through the registration view. Orders can be placed through the cart view.

## Technologies

| Technology | Purpose |
|---|---|
| Spring Boot | Backend REST API |
| Vue.js | Frontend framework |
| PostgreSQL | Database |
| Docker | Containerized database setup |
| Gradle | Build automation |

## Prerequisites

- Java 21
- Node.js 18+ and npm
- PostgreSQL 16
- Docker Desktop
- Git

## Features

| Feature | Description |
|---|---|
| Product browsing | Browse and search products by name and price |
| Shopping cart | Add products and place orders |
| User accounts | Register and log in |
| REST API | Full backend API with Spring Boot |
| Integration tests | Testcontainers-based integration tests |

## Authors

Janne-Lii Aun-Trepp and Kristin Vares

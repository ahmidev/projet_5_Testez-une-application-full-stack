
# Yoga Studio Application


## Installation

### 1. Clonez le dépôt Git :

```bash
git clone https://github.com/ahmidev/projet_5_Testez-une-application-full-stack.git
```

### 2. Accédez au dossier du projet :

```bash
cd Testez-une-application-full-stack
```

## Installation du Front-End

### 1. Accédez au dossier du front-end :

```bash
cd front
```

### 2. Installez les dépendances :

```bash
npm install
```

### 3. Lancez le front-end :

```bash
npm run start
```

Le front-end sera disponible à l’adresse [http://localhost:4200](http://localhost:4200).

## Installation du Back-End

### 1. Importez le projet dans IntelliJ IDEA ou votre IDE préféré.

### 2. Configurez la base de données MySQL :

Le script SQL pour créer le schéma est disponible ici : `ressources/sql/script.sql`.

### 3. Configurez les variables d’environnement :

Modifiez le fichier `application.properties`  pour inclure vos configurations.

### 4. Lancez l’application Spring Boot :

```bash
mvn spring-boot:run
```

Le back-end sera disponible à l’adresse [http://localhost:8080](http://localhost:8080).

## Compte Administrateur

- **Login** : `yoga@studio.com`
- **Mot de passe** : `test!1234`

## Lancer les Tests

### Front-End

1. **Tests Unitaires** :

```bash
npm run test
```

2. **Rapport de couverture des tests unitaires** :

```bash
npm run test:coverage
```

Le rapport HTML sera disponible ici : `/front/coverage/jest/lcov-report/index.html`.

![Rapport de couverture des tests unitaires ](/ressources/front-jest.png)

![Rapport de couverture des tests unitaires ](/ressources/front-jestb.png)


3. **Tests End-to-End (E2E)** :

Lancez les tests E2E avec :

```bash
npm cypress:run
```

Ou avec :

```bash
npm run e2e
```
4. **Rapport de couverture des tests End-to-End (E2E)** :

```bash
npm run "e2e:coverage":
```

Le rapport HTML sera disponible ici : `/front/coverage/lcov-report/index.html`.

![Rapport de couverture des tests End-to-End (E2E) ](/ressources/front-e2e.png)

![Rapport de couverture des tests  End-to-End (E2E) ](/ressources/front-e2eb.png)


### Back-End

1. Exécutez les tests avec Maven :

```bash
mvn clean test
```

2. Génération du rapport de couverture avec JaCoCo :

Le rapport HTML sera disponible ici : `back/target/site/jacoco/index.html`.

![Rapport de couverture des tests  back ](/ressources/back.png)
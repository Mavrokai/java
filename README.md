# My_Restaurant

Outil « clé en main » pour restaurateurs : configuration visuelle du site, gestion
du contenu (carte, menus, plats, allergènes) via un back-office protégé, et site
public destiné aux clients (accueil, carte, menus, avis).

Thème de démonstration : **restaurant japonais épuré**.

## Stack

- **Java 17** / **Spring Boot 3.3**
- **Spring MVC + Thymeleaf** (rendu serveur)
- **Spring Data JPA** + **MySQL 8**
- **Spring Security** (espace admin protégé par mot de passe)
- **Bean Validation**

## Démarrage rapide

### 1. Base de données

Avec Docker (recommandé) :

```bash
docker compose up -d
```

Cela démarre MySQL 8 avec la base `my_restaurant` et l'utilisateur
`my_restaurant` / `my_restaurant` (voir `docker-compose.yml`).

> Sans Docker, avec un MySQL local déjà installé :
>
> ```bash
> sudo mysql <<'SQL'
> CREATE DATABASE IF NOT EXISTS my_restaurant CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
> CREATE USER IF NOT EXISTS 'my_restaurant'@'localhost' IDENTIFIED BY 'my_restaurant';
> GRANT ALL PRIVILEGES ON my_restaurant.* TO 'my_restaurant'@'localhost';
> FLUSH PRIVILEGES;
> SQL
> ```

### 2. Lancer l'application

```bash
mvn spring-boot:run
```

Le site est accessible sur http://localhost:8080.

> **Démarrage immédiat sans MySQL** : `mvn spring-boot:run` active automatiquement
> le profil `dev` qui utilise une base **H2 fichier** (`./data`), donc aucune
> installation de base n'est nécessaire pour développer. Le **jar packagé**
> (`mvn package` puis `java -jar`) et **Docker** utilisent **MySQL** (profil par défaut),
> conformément au sujet.
>
> Pour forcer MySQL en local : `mvn spring-boot:run -Dspring-boot.run.profiles=default`
> (après avoir créé la base, cf. ci-dessous).

### 3. Première configuration

Tant que le site n'est pas configuré, **toute URL redirige vers `/setup`**.
Renseignez :

- Nom du restaurant, URL, couleur d'accent, bannière (upload optionnel)
- Mot de passe administrateur (min. 8 caractères, 1 minuscule, 1 majuscule, 1 chiffre)
- Adresse + informations de contact

Une fois validé, le site public est en ligne et l'espace admin accessible.

### 4. Espace administrateur

- Connexion : http://localhost:8080/login (mot de passe défini à l'étape 3)
- Tableau de bord : `/admin`
- Gérer : **la carte**, **les menus**, **les plats** (avec allergènes et
  assignation aux sections), **les avis**.

## Contenu de démonstration

Au premier démarrage (si aucun plat n'existe), un jeu de données japonais est
créé automatiquement : carte (Entrées, Sushis & Makis, Plats chauds, Desserts),
menus « Découverte » et « Sakura », et 17 plats avec leurs allergènes.

> La **configuration du site** (`/setup`) reste à réaliser manuellement.

## Images

Les vraies photos seront uploadées plus tard. En attendant, un endpoint
`/placeholder` génère à la volée des images SVG (hors-ligne) dont la couleur
dérive du nom du plat/restaurant.

## Modèle de données

- `RestaurantConfig` (singleton) — config visuelle + infos restaurant + mot de passe admin (BCrypt)
- `Menu` — la **carte** est l'unique `Menu` avec `carte = true` ; les autres sont des menus
- `Section` — appartient à un menu/carte, contient des plats (many-to-many)
- `Dish` — nom, photo, description, prix, allergènes (14 allergènes réglementaires)
- `Review` — avis client (nom, email optionnel, date, note étoiles, commentaire)

## Tests

```bash
mvn test
```

Les tests s'exécutent sur une base **H2 en mémoire** (profil `test`) et vérifient
le chargement du contexte, le seed et le rendu de toutes les pages.

## Notes techniques

- `spring.jpa.open-in-view=true` : activé pour permettre le rendu des collections
  lazy (sections, plats) dans les vues Thymeleaf.
- Les fichiers uploadés sont stockés dans `./uploads` et servis sous `/uploads/**`.
- Fonctionnalités bonus du sujet (réservation, formulaire de contact avancé,
  like/dislike sur les avis) **non implémentées**.

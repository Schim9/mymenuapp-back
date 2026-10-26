# MenuApp Backend

## Stack
- Java 21 (preview features activées) + Spring Boot 3.4.3
- MySQL 5.7+ / Spring Data JPA + Hibernate
- MapStruct 1.6.3 + Lombok
- Log4j2 (pas Logback)
- Build : Maven (`./mvnw`)

## Structure des packages (`lu.perso.menuback`)
- `controller/` — REST controllers (`/dishes`, `/menus`, `/ingredients`, `/admin`, `/shopping-list`)
- `services/` — logique métier
- `models/` — records Java (DTOs)
- `data/` — entités JPA (`*Entity`)
- `mappers/` — interfaces MapStruct (`DishMapper`, `IngredientMapper`) + `MenuMapper` (`@Component` Spring, injection par constructeur — pas un mapper MapStruct)
- `repository/` — interfaces Spring Data JPA (`JpaRepository` — tous les repos étendent `JpaRepository`, pas `CrudRepository`)
- `config/` — WebConfig (CORS), UserCodeFilter (auth par user-code)
- `constant/` — MenuEnum
- `util/` — utilitaires transverses (`DateUtil` : calcul uptime)

## Commandes utiles
```bash
./mvnw clean package -DskipTests   # build JAR
./mvnw spring-boot:run              # lancer en dev
```

## Contexte d'infrastructure

Cette application tourne sur le Pi **appsPi2** (192.168.0.86). Charger la skill `/info-pi-apps2` pour avoir le contexte complet du serveur (déploiement, services, ports, etc.) avant toute intervention liée à l'infrastructure.

## Notes importantes
- Les preview features Java sont activées (`--enable-preview`) dans le compiler, surefire et le plugin boot
- Auth via header `X-User-Code` filtré par `UserCodeFilter`
- Logs dans `logs/app.log` (rolling, 10 MB max, 10 fichiers)
- Déploiement cible : Raspberry Pi (JAR avec config externe)

## Configuration sensible

`application.yml` ne contient aucune valeur secrète — uniquement des placeholders `${VAR}`.
Les vraies valeurs sont dans un `application-local.yml` hors dépôt (gitignored).

Variables requises :
- `DB_URL` — optionnel, défaut : `jdbc:mysql://localhost:3306/menu-app?serverTimezone=UTC`
- `DB_USERNAME` — login MySQL
- `DB_PASSWORD` — mot de passe MySQL
- `APP_USER_CODE` — code d'auth front
- `CORS_ORIGINS` — optionnel, défaut : `http://localhost:3000,https://menu-app.kaminski.lu`

Lancer avec config externe :
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="--spring.config.additional-location=file:///chemin/vers/config/"
# ou pour le JAR :
java -jar menu-back.jar --spring.config.additional-location=file:///chemin/vers/config/
```

## Feature Shopping List

Liste de courses générée depuis les menus. Le front sélectionne les ingrédients manquants et les pousse en base ; une fois les courses faites, il vide la liste.

- Table : `shopping_list`
- Entité : `ShoppingItemEntity`
- Endpoints (`/shopping-list`) :
  - `GET` — lire la liste courante
  - `POST` — ajouter des items (sans doublons)
  - `DELETE` — vider la liste (courses effectuées)

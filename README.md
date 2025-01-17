#Batch d'import des données BDREP

## 1.	Prérequis : 

La machine sur laquelle le batch sera installé devra avoir au minimum 16Go de RAM. PostGreSQL et Java 17 (minimum) doivent être installés.  

**Installation de la base de test :**  

Créer une nouvelle base de données sur une instance postgres  
Activer les extensions postgis et postgis-topology  
Exécuter le script **create_tables.sql** (création de la base de données temporaires, schéma geremi_batch) puis le script **insert_bdrep.sql** (création de la base de données définitive).  

**Exécution du batch :**  

Dans le répertoire de votre choix, copier le jar target/bdrep-batch-0.0.1-SNAPSHOT.jar.  
Ajouter au même niveau un fichier **application.properties** dans lequel on indiquera l'URL de connexion à la base de données test, le chemin du fichier source et les chemins vers les fichiers d'erreur temporaire et final.  
  
 
Pour exécuter le batch : se positionner dans le répertoire où il a été installé et exécuter la commande :  

java -jar target/bdrep-batch-0.0.1-SNAPSHOT.jar  

## 2.	Fonctionnement détaillé du batch
### 2.1.	Récupération du fichier source
On récupère le fichier source à l’emplacement spécifié dans application.properties. Si le fichier ne se trouve pas à l’endroit indiqué, une erreur est générée et l’exécution du batch est suspendue.

### 2.2.	Enregistrement de chaque onglet dans des tables temporaires
Chacun des onglets est enregistré dans une table temporaire (schema geremi_batch). Les données ne sont pas converties et restent au format VARCHAR.

### 2.3.	Vérification des règles
Les règles définies pour chaque onglet sont vérifiées. Pour chaque règle bloquante non validée, une ligne est inscrite dans un fichier .txt qui sera fourni au client pour qu’il puisse corriger le fichier source en fonction des erreurs trouvées. Si des règles non bloquantes ne sont pas validées, les entrées correspondantes sont créées dans la table anomalie.
Si des règles bloquantes sont présentes, l’exécution du batch est stoppée. 
Si des règles non bloquantes sont présentes, l’exécution du batch se poursuit avec l’enregistrement définitif en base.

### 2.4.	Enregistrement définitif en base
Les données définitives sont enregistrées en base (schema geremi),en fonction des filtres définis dans les règles (R14 ou R15 en fonction des onglets). Les données qui étaient auparavant au format VARCHAR sont typées comme défini dans le modèle de données, les informations millésimées sont sauvegardées dans des tables distinctes.

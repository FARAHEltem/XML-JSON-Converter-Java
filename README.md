#  XML-JSON Converter (Java & JavaFX)

Ce projet est un convertisseur de données bidirectionnel capable de transformer du **XML en JSON** et du **JSON en XML**. Il a été conçu pour démontrer la manipulation de structures de données complexes via des algorithmes manuels et l'utilisation de bibliothèques standards.

##  Démonstration Vidéo
Découvrez le fonctionnement de l'application, l'explication du code et les tests en cliquant sur le lien ci-dessous :

 [**Regarder la vidéo de démonstration (Google Drive)**](https://drive.google.com/file/d/1P1y53PqZ_jWb8GvoIqWeOy1io7s7vqYF/view?usp=sharing)

---

##  Fonctionnalités
* **Conversion Bidirectionnelle** : Passage fluide entre les formats XML et JSON.
* **Moteur de Parsing Manuel** : Implémentation d'algorithmes personnalisés sans dépendance externe pour le traitement de base.
* **Récursion Profonde** : Gestion des structures imbriquées (nested) sans limite de niveau.
* **Validation & Erreurs** : Détection des balises mal fermées et des erreurs de syntaxe.
* **Interface JavaFX** : Interface utilisateur moderne pour coller, convertir et visualiser les résultats en temps réel.
* **Mode API Jackson** : Option pour utiliser la bibliothèque Jackson afin de comparer les résultats avec le moteur manuel.

---

##  Architecture Technique

### 1. Algorithme de Récursion
Le cœur de la conversion manuelle repose sur la récursion. La classe `ConverterService` explore l'arborescence des données :
* Pour le **XML**, il identifie les balises enfants et s'appelle lui-même pour traiter les sous-niveaux.
* Pour le **JSON**, il parcourt les Maps et Listes pour reconstruire les balises XML correspondantes.

### 2. Expressions Régulières (Regex)
Le découpage du XML est effectué à l'aide de patterns Regex optimisés pour capturer :
* Le nom des balises : `<(\\w+)>`
* Le contenu entre les balises : `(.*?)</\\1>`

### 3. Comparaison : Manuel vs Jackson
L'application permet de comparer le comportement du moteur manuel (qui préserve souvent mieux la structure de la racine) avec le standard industriel fourni par l'API Jackson.

---

## 🛠️ Technologies Utilisées
* **Langage** : Java 17+
* **Interface Graphique** : JavaFX
* **Gestion des Dépendances** : Maven
* **Bibliothèque JSON** : Jackson Databind (pour le mode automatique)



Fonctionnalités à développer
✅ 1. Vérification et ajout d’un prêt
But : Ajouter un prêt si les conditions sont remplies.
Règles de gestion :

Un utilisateur ne peut pas avoir plus de quota.nbr_livre_max_pret livres prêtés en même temps.

La date de retour est fixée à quota.nbr_jour_max_pret jours après la date du prêt.

Si le prêt est "sur place", la durée est de 1 jour et ne compte pas dans le quota.

Le livre doit avoir un ou plusieurs exemplaires disponibles dans exemplaire (où action=true).

Entrées :

id_user, id_livre, nombre d'exemplaires, surPlace (booléen)

Traitement :

Vérifier que l'utilisateur est abonné (User.id_abonnement != null)

Vérifier qu’il n’a pas atteint le nombre maximum de livres en prêt (hors surPlace)

Vérifier les exemplaires disponibles

Insérer le prêt dans pret

✅ 2. Affichage de la liste des prêts en cours d’un utilisateur
But : Permettre à un membre de voir ses prêts non encore retournés.
Règles :

On utilise pret + status_pret pour détecter les prêts non terminés (pas de statut "retourné").

Affichage trié par date de prêt décroissante.

✅ 3. Prolongement d’un prêt
But : Permettre à un utilisateur de prolonger un prêt selon ses droits.

Règles de gestion :

L’utilisateur peut prolonger un prêt si le nombre de prolongements déjà effectués < quota.nbr_prologement_max

Chaque prolongement ajoute quota.nbr_jour_max_prolongement jours

Le prolongement doit être demandé au moins quota.nbr_jour_avant_prologement jours avant la date de retour actuelle

Entrées :

id_pret

Traitement :

Vérifier les règles ci-dessus

Ajouter une entrée dans status_pret avec le statut "Prolongé"

Modifier la date de retour prévue

✅ 4. Détection et génération automatique de pénalités
But : Créer une pénalité si un livre n’est pas retourné à temps.

Règles :

Si la date actuelle > date limite de retour (datePret + durée + prolongements)

Ajouter une ligne dans penalite avec le nombre de jours de retard

Traitement (tâche planifiée ou déclenchée par affichage) :

Vérifier tous les prêts en cours

Si en retard → ajouter une pénalité

✅ 5. Retour de livre
But : Enregistrer le retour d’un ou plusieurs livres prêtés

Règles de gestion :

Mise à jour dans status_pret avec le statut "Retourné"

Ajout d’une entrée exemplaire avec action = true, nbr = x (nombre retourné)

Vérifier si une pénalité doit être appliquée

Entrées :

id_pret, nombre retourné


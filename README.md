# boursycrypto_gdax_microservices

Projet Boursycrypto dans une architecture plus "microservices"

# Avant de lancer l'application pour la première fois
Il faut créér la BDD de influxdb via la commande
```bash
    docker volume create influx_data
    docker run --rm -e INFLUXDB_DB=db0 -v influx_data:/var/lib/influxdb influxdb /init-influxdb.sh
```

# Pour lancer l'application
```bash
    cd deploy
    docker-compose up
```

# Se connecter a telegraph (IHM suivi application)
URL : http://localhost:8888

A la première connexion il sera demandé : 
    - influx URL = http://influxdb:8086
    - db name = db0
    - Laisser le login et password vide
Ensuite aller créer la db 0 dans l'IHM
    
# RouletteProject

_El proyecto contiene microservicios asociados a una ruleta_

## Autor ✒️

* **Bryan Velandia** - [bdvelandiap](https://github.com/bdvelandiap)

### Tecnologías 📋
```
Springboot
```
```
Redis
```
### Endpints ⚙️
* POST http://localhost:9292/roulette/save       Realiza la creación de una nueva ruleta
* GET  http://localhost:9292/roulette/all        Da un listado de las ruletas creadas
* POST http://localhost:9292/roulette/open/{id}  Cambia estado de la ruleta a "abierta" según el id
* POST http://localhost:9292/bet/save            Guarda una apuesta a un número o un color
* GET  http://localhost:9292/bet/closeBet/{id}   Cierra y "juega" las apuestas creadas asociadas a la ruleta según el id
* GET  http://localhost:9292/bet/all             Da un listado del historial de apuestas

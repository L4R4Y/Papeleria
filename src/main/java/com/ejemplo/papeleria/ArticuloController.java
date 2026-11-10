package com.ejemplo.papeleria;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/articulos")
public class ArticuloController {

    private List<Articulo> articulos = new ArrayList<>();
    private int siguienteId = 1;


    public ArticuloController(){
        articulos.add(new Articulo(siguienteId++, "cuaderno", 4500.0, 30));
        articulos.add(new Articulo(siguienteId++, "Lapíz", 1200.0, 100));
        articulos.add(new Articulo(siguienteId++, "Borrador", 800.0, 5));

    }

    private Articulo buscarPorId(int id){
        for (Articulo articulo: articulos){
            if (articulo.getId() == id){
                return articulo;
            }
        }
        return null;
    }

    @GetMapping
    public List<Articulo> listarArticulos(){
        return articulos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Articulo> obtenerArticulo(@PathVariable int id){
        Articulo articulo = buscarPorId(id);
        if (articulo != null){
            return ResponseEntity.ok(articulo);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/bajo-stock/{limite}")
    public List<Articulo> listarBajoStock(@PathVariable int limite){
        List <Articulo> resultado = new ArrayList<>();

        for (Articulo articulo: articulos){
            if (articulo.getStock() < limite){
                resultado.add(articulo);
            }
        }
        return resultado;
    }

    @PostMapping
    public ResponseEntity<Articulo> crearArticulo(@RequestBody Articulo articulo){

        System.out.println("Nombre: " + articulo.getNombre());
        System.out.println("Precio: " + articulo.getPrecio());
        System.out.println("Stock: " + articulo.getStock());

        if (articulo.getNombre() == null || articulo.getNombre().isBlank()
                || articulo.getPrecio() < 0 || articulo.getStock() < 0) {

            System.out.println("El articulo no paso la validacion");
            return ResponseEntity.badRequest().build();
        }

        articulo.setId(siguienteId++);
        articulos.add(articulo);

        return ResponseEntity.status(201).body(articulo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Articulo> actualizarArticulo(
            @PathVariable Integer id,
            @RequestBody Articulo datosArticulo) {

        Articulo articulo = buscarPorId(id);

        if (articulo == null) {
            return ResponseEntity.notFound().build();
        }

        if (datosArticulo.getNombre() == null || datosArticulo.getNombre().isBlank()
                || datosArticulo.getPrecio() < 0
                || datosArticulo.getStock() < 0) {

            return ResponseEntity.badRequest().build();
        }

        articulo.setNombre(datosArticulo.getNombre());
        articulo.setPrecio(datosArticulo.getPrecio());
        articulo.setStock(datosArticulo.getStock());

        return ResponseEntity.ok(articulo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArticulo(@PathVariable Integer id) {

        Articulo articulo = buscarPorId(id);

        if (articulo == null) {
            return ResponseEntity.notFound().build();
        }

        articulos.remove(articulo);

        return ResponseEntity.noContent().build();
    }
}

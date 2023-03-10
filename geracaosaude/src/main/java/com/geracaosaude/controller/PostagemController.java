package com.geracaosaude.controller;

import com.geracaosaude.model.Postagem;
import com.geracaosaude.model.Tema;
import com.geracaosaude.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagem")
@CrossOrigin(value = "*", allowedHeaders = "*")

public class PostagemController {
    @Autowired
    private PostagemRepository postagemRepository;

    @GetMapping
    public ResponseEntity<List<Postagem>> getAll(){ return ResponseEntity.ok(postagemRepository.findAll());}

    @GetMapping("/{id}")
    public ResponseEntity<Postagem> getById(@PathVariable Long id)
    {return postagemRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
        return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
    }

    @PostMapping
    public ResponseEntity<Postagem> post(@RequestBody @Valid Postagem postagem){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postagemRepository.save(postagem));
    }

    @PutMapping
    public ResponseEntity<Postagem> put(@RequestBody @Valid Postagem postagem){
        return postagemRepository.findById(postagem.getId())
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(postagemRepository.save(postagem)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        Optional<Postagem> postagem = postagemRepository.findById(id);
        if(postagem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        postagemRepository.deleteById(id);
    }
}

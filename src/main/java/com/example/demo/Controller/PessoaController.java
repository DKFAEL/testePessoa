package com.example.demo.Controller;

import com.example.demo.Domain.Endereco;
import com.example.demo.Domain.Pessoa;
import com.example.demo.Repository.EnderecoRepository;
import com.example.demo.Repository.PessoaRepository;
import com.example.demo.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private PessoaService pessoaService;

    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody Pessoa pessoa) {
        Pessoa pessoaCriada = pessoaService.criarPessoa(pessoa);
        return ResponseEntity.created(URI.create("/pessoas/" + pessoaCriada.getId())).body(pessoaCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> editarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        Pessoa pessoaEditada = pessoaService.editarPessoa(id, pessoa);
        if (pessoaEditada != null) {
            return ResponseEntity.ok(pessoaEditada);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> consultarPessoa(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.consultarPessoa(id);
        if (pessoa != null) {
            return ResponseEntity.ok(pessoa);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        List<Pessoa> pessoas = pessoaService.listarPessoas();
        return ResponseEntity.ok(pessoas);
    }

    @PostMapping("/{id}/enderecos")
    public ResponseEntity<Endereco> criarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        Endereco enderecoCriado = pessoaService.criarEndereco(id, endereco);
        if (enderecoCriado != null) {
            return ResponseEntity.created(URI.create("/pessoas/" + id + "/enderecos/" + enderecoCriado.getId())).body(enderecoCriado);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/enderecos")
    public ResponseEntity<List<Endereco>> listarEnderecos(@PathVariable Long id) {
        List<Endereco> enderecos = pessoaService.listarEnderecos(id);
        return ResponseEntity.ok(enderecos);
    }

    @PutMapping("/{id}/enderecos/{enderecoId}")
    public ResponseEntity<Endereco> editarEndereco(@PathVariable Long id, @PathVariable Long enderecoId, @RequestBody Endereco endereco) {
        Endereco enderecoEditado = pessoaService.editarEndereco(id, enderecoId, endereco);
        if (enderecoEditado != null) {
            return ResponseEntity.ok(enderecoEditado);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/endereco/{enderecoId}")
    public ResponseEntity<Endereco> definirEnderecoPrincipal(@PathVariable Long id, @PathVariable Long enderecoId) {
        Endereco enderecoDefinido = pessoaService.definirEnderecoPrincipal(id, enderecoId);
        if (enderecoDefinido != null) {
            return ResponseEntity.ok(enderecoDefinido);
        }
        return ResponseEntity.notFound().build();
    }
}





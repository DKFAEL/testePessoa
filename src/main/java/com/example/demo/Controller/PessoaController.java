package com.example.demo.Controller;

import com.example.demo.Domain.Endereco;
import com.example.demo.Domain.Pessoa;
import com.example.demo.Repository.EnderecoRepository;
import com.example.demo.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody Pessoa pessoa) {
        Pessoa pessoaCriada = pessoaRepository.save(pessoa);
        return ResponseEntity.created(URI.create("/pessoas/" + pessoaCriada.getId())).body(pessoaCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> editarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        Optional<Pessoa> pessoaExistenteOptional = pessoaRepository.findById(id);
        if (pessoaExistenteOptional.isPresent()) {
            pessoa.setId(id);
            Pessoa pessoaEditada = pessoaRepository.save(pessoa);
            return ResponseEntity.ok(pessoaEditada);
        }
        return ResponseEntity.notFound().build();
    };

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> consultarPessoa(@PathVariable Long id) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            return ResponseEntity.ok(pessoa);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return ResponseEntity.ok(pessoas);
    }

    @PostMapping("/{id}/enderecos")
    public ResponseEntity<Endereco> criarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            endereco.setPessoa(pessoa);
            Endereco enderecoCriado = enderecoRepository.save(endereco);
            return ResponseEntity.created(URI.create("/pessoas/" + id + "/enderecos/" + enderecoCriado.getId())).body(enderecoCriado);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/enderecos")
    public ResponseEntity<List<Endereco>> listarEnderecos(@PathVariable Long id) {
        List<Endereco> enderecos = enderecoRepository.findByPessoaId(id);
        return ResponseEntity.ok(enderecos);
    }

    @PutMapping("/{id}/enderecos/{enderecoId}")
    public ResponseEntity<Endereco> editarEndereco(@PathVariable Long id, @PathVariable Long enderecoId, @RequestBody Endereco endereco) {
        Optional<Endereco> enderecoExistenteOptional = enderecoRepository.findById(enderecoId);
        if (enderecoExistenteOptional.isPresent()) {
            Endereco enderecoExistente = enderecoExistenteOptional.get();
            if (!enderecoExistente.getPessoa().getId().equals(id)) {
                return ResponseEntity.badRequest().build();
            }
            endereco.setId(enderecoId);
            Endereco enderecoEditado = enderecoRepository.save(endereco);
            return ResponseEntity.ok(enderecoEditado);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/enderecos/{enderecoId}/principal")
    public ResponseEntity<Endereco> definirEnderecoPrincipal(@PathVariable Long id, @PathVariable Long enderecoId) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            Optional<Endereco> enderecoOptional = enderecoRepository.findByPessoaIdAndPrincipalIsTrue(id);
            if (enderecoOptional.isPresent()) {
                Endereco enderecoAntigo = enderecoOptional.get();
                if (enderecoAntigo.getId().equals(enderecoId)) {
                    return ResponseEntity.badRequest().build();
                }
                enderecoAntigo.setPrincipal(false);
                enderecoRepository.save(enderecoAntigo);
            }
            Optional<Endereco> enderecoNovoOptional = enderecoRepository.findById(enderecoId);
            if (enderecoNovoOptional.isPresent()) {
                Endereco enderecoNovo = enderecoNovoOptional.get();
                if (!enderecoNovo.getPessoa().getId().equals(id)) {
                    return ResponseEntity.badRequest().build();
                }
                enderecoNovo.setPrincipal(true);
                enderecoRepository.save(enderecoNovo);
                return ResponseEntity.ok(enderecoNovo);
            }
        }
        return ResponseEntity.notFound().build();
    }
}


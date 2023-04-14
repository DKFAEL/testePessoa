

package com.example.demo.Service;

import com.example.demo.Domain.Endereco;
import com.example.demo.Domain.Pessoa;
import com.example.demo.Repository.EnderecoRepository;
import com.example.demo.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Pessoa criarPessoa(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Pessoa editarPessoa(Long id, Pessoa pessoa) {
        Optional<Pessoa> pessoaExistenteOptional = pessoaRepository.findById(id);
        if (pessoaExistenteOptional.isPresent()) {
            pessoa.setId(id);
            return pessoaRepository.save(pessoa);
        }
        return null;
    }

    public Pessoa consultarPessoa(Long id) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        return pessoaOptional.orElse(null);
    }

    public List<Pessoa> listarPessoas() {
        return pessoaRepository.findAll();
    }

    public Endereco criarEndereco(Long pessoaId, Endereco endereco) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            endereco.setPessoa(pessoa);
            return enderecoRepository.save(endereco);
        }
        return null;
    }

    public List<Endereco> listarEnderecos(Long pessoaId) {
        return enderecoRepository.findByPessoaId(pessoaId);
    }

    public Endereco editarEndereco(Long pessoaId, Long enderecoId, Endereco endereco) {
        Optional<Endereco> enderecoExistenteOptional = enderecoRepository.findById(enderecoId);
        if (enderecoExistenteOptional.isPresent()) {
            Endereco enderecoExistente = enderecoExistenteOptional.get();
            if (!enderecoExistente.getPessoa().getId().equals(pessoaId)) {
                return null;
            }
            endereco.setId(enderecoId);
            return enderecoRepository.save(endereco);
        }
        return null;
    }

    public Endereco definirEnderecoPrincipal(Long pessoaId, Long enderecoId) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            Optional<Endereco> enderecoAntigoOptional = enderecoRepository.findByPessoaIdAndPrincipalIsTrue(pessoaId);
            if (enderecoAntigoOptional.isPresent()) {
                Endereco enderecoAntigo = enderecoAntigoOptional.get();
                if (enderecoAntigo.getId().equals(enderecoId)) {
                    return null;
                }
                enderecoAntigo.setPrincipal(false);
                enderecoRepository.save(enderecoAntigo);
            }
            Optional<Endereco> enderecoNovoOptional = enderecoRepository.findById(enderecoId);
            if (enderecoNovoOptional.isPresent()) {
                Endereco enderecoNovo = enderecoNovoOptional.get();
                if (!enderecoNovo.getPessoa().getId().equals(pessoaId)) {
                    return null;
                }
                enderecoNovo.setPrincipal(true);
                enderecoRepository.save(enderecoNovo);
                return enderecoNovo;
            }
        }
        return null;
    }
}


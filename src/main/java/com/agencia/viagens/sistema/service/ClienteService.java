package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.repository.ClienteRepository;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repository;


    @Transactional
    public List<Cliente> buscarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return repository.findByCpfOrPassaporte(documento, documento);
    }

    @Transactional
    public List<Cliente> buscar(String query) {
        return repository.buscar(query);
    }

    @Transactional
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void removerCliente(Cliente cliente) {
        repository.delete(cliente);
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente) throws ValidationException {
        if (cliente.getNome().isEmpty()) throw new ValidationException("Nome não pode ser vazio!");
        if (cliente.getTelefone().isEmpty()) throw new ValidationException("Telefone não pode ser vazio!");

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phone;
        try {
            phone = phoneUtil.parse(cliente.getTelefone(), "BR");
        } catch (NumberParseException ex) {
            throw new ValidationException("Telefone invalido!");
        }

        cliente.setTelefone(
                phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        );


        if (cliente.getEmail().isEmpty()) throw new ValidationException("O Email do cliente não pode ser vazio!");
        if (StringUtils.countMatches(cliente.getEmail(), '@') != 1)
            throw new ValidationException("Email invalido!");

        return repository.save(cliente);
    }
}

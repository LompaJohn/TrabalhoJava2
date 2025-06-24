package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.repository.ClienteRepository;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repository;
    private static final Pattern emailRegEx = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    private static final Pattern cpfRegEx = Pattern.compile("^\\d{3}(?:\\.|)\\d{3}(?:\\.|)\\d{3}(?:-|)\\d{2}$");
    private static final Pattern passaporteRegEx = Pattern.compile("^[A-Za-z0-9]{6,12}$");

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
    public void removerPorId(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente) throws ValidationException {
        // nome
        String nome = cliente.getNome().trim();

        if (nome.isEmpty())
            throw new ValidationException("Nome não pode ser vazio!");

        cliente.setNome(nome);

        // telefone

        if (cliente.getTelefone().trim().isEmpty())
            throw new ValidationException("Telefone não pode ser vazio!");

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phone;

        try {
            phone = phoneUtil.parse(cliente.getTelefone(), "BR");
        } catch (NumberParseException ex) {
            throw new ValidationException("Telefone invalido!");
        }

        if (!phoneUtil.isValidNumber(phone))
            throw new ValidationException("Telefone invalido!");

        cliente.setTelefone(
                phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));

        // email

        String email = cliente.getEmail().trim();

        if (email.isEmpty())
            throw new ValidationException("O Email do cliente não pode ser vazio!");

        if (!emailRegEx.matcher(email).matches())
            throw new ValidationException("Email invalido!");

        cliente.setEmail(email);

        // documento

        switch (cliente.getTipo()) {
            case ESTRANGEIRO: {
                String passaporte = cliente.getPassaporte().trim();

                if (!passaporteRegEx.matcher(passaporte).matches())
                    throw new ValidationException("Passaporte invalido!");

                cliente.setPassaporte(passaporte);
                break;
            }
            case NACIONAL: {
                String cpfFormatado = validarEFormatarCPF(cliente.getCpf().trim());

                if (cpfFormatado == null)
                    throw new ValidationException("CPF invalido!");

                cliente.setCpf(cpfFormatado);

                break;
            }
        }

        return repository.save(cliente);
    }

    private static String validarEFormatarCPF(String cpf) {
        if (cpf == null || cpf.isEmpty() || !cpfRegEx.matcher(cpf).matches())
            return null;

        String cpfNumerico = cpf.replaceAll("[^0-9]", "");

        if (cpfNumerico.length() != 11)
            return null;

        // todos os numeros nao podem ser iguais
        if (cpfNumerico.matches("(\\d)\\1{10}"))
            return null;

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cpfNumerico.charAt(i));
            soma += digito * (10 - i);
        }

        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : (11 - resto);

        soma = 0;

        for (int i = 0; i < 10; i++) {
            int digito = Character.getNumericValue(cpfNumerico.charAt(i));
            soma += digito * (11 - i);
        }

        resto = soma % 11;

        int digito2 = (resto < 2) ? 0 : (11 - resto);

        if (digito1 != Character.getNumericValue(cpfNumerico.charAt(9)) ||
                digito2 != Character.getNumericValue(cpfNumerico.charAt(10))) {
            return null;
        }

        return cpfNumerico.substring(0, 3) + "." +
                cpfNumerico.substring(3, 6) + "." +
                cpfNumerico.substring(6, 9) + "-" +
                cpfNumerico.substring(9, 11);
    }

}

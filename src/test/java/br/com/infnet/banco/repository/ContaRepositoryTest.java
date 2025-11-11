package br.com.infnet.banco.repository;

import br.com.infnet.banco.entity.Conta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContaRepositoryTest {

    @Autowired
    private ContaRepository contaRepository;

    @Test
    void deveSalvarEConsultar() {
        Conta conta = new Conta(null, "Teste", new BigDecimal("150.00"));
        Conta salva = contaRepository.save(conta);

        assertNotNull(salva.getId());
        assertEquals("Teste", salva.getTitular());
    }
}

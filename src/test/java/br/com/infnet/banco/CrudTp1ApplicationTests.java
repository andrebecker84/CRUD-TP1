package br.com.infnet.banco;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(args = "skip-cli")
@ActiveProfiles("h2")
class CrudTp1ApplicationTests {

    @Test
    void contextLoads() {
        // Teste basico que verifica se o contexto Spring carrega corretamente
    }

}

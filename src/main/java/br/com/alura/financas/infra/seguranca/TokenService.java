package br.com.alura.financas.infra.seguranca;

import br.com.alura.financas.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.segredo}")
    private String segredo;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.create()
                    .withIssuer("API de Finanças")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token jwt", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusMinutes(20).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.require(algoritmo)
                    .withIssuer("API de Finanças")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    public Date getExpiresAt(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.require(algoritmo)
                    .withIssuer("API de Finanças")
                    .build()
                    .verify(tokenJWT)
                    .getExpiresAt();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

}

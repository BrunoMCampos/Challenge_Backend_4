package br.com.alura.financas.controller;

import br.com.alura.financas.domain.usuario.DadosAltenticacao;
import br.com.alura.financas.domain.usuario.Usuario;
import br.com.alura.financas.domain.usuario.UsuarioRepository;
import br.com.alura.financas.infra.exception.DadosErro;
import br.com.alura.financas.infra.seguranca.DadosTokenJWT;
import br.com.alura.financas.infra.seguranca.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAltenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        String tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @GetMapping
    public void testarToken() {
    }

    @PostMapping("/renew")
    public ResponseEntity renovarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.replace("Bearer ", "");

        Date expiresAt = tokenService.getExpiresAt(token);

        if (LocalDateTime.now().plusMinutes(5).isBefore(LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault()))) {
            return ResponseEntity.badRequest().body(new DadosErro("Token", "Token impossível de ser renovado - Tempo de expiração maior que 5 minutos"));
        } else {
            String login = tokenService.getSubject(token);

            Usuario usuario = (Usuario) usuarioRepository.findByLogin(login);
            String newToken = tokenService.gerarToken(usuario);
            return ResponseEntity.ok(new DadosTokenJWT(newToken));
        }

    }

}

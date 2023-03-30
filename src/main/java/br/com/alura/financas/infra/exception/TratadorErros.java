package br.com.alura.financas.infra.exception;

import br.com.alura.financas.validacao.ValidacaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorErros {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> tratandoErro500PorValidacao(ValidacaoException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratandoErro400(MethodArgumentNotValidException ex){
        List<FieldError> errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> tratandoErro400PorTipoDeEnvio(HttpMessageNotReadableException ex){
        return ResponseEntity.badRequest().body("Dados incorretos ou mal formulados");
    }
    private record DadosErroValidacao(String campo, String message){
        public DadosErroValidacao(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }
}

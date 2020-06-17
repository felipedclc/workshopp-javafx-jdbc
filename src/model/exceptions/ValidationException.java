package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException { // TEM A FUNÇAO DE CARREGAR AS MENSAGENS DE ERRO

	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>(); // GUARDANDO NOS CAMPOS OS NOMES/ID E OS ERROS CORRESPONDENTES

	public ValidationException(String msg) { // FORÇANDO A VALIDAÇÃO DA INSTANCIAÇÃO COM STRING
		super(msg);
	}

	public Map<String, String> getErros() {
		return errors;
	}

	public void addErro(String fieldName, String errorMensage) { // ADICIONANDO POSSIVEIS ERROS
		errors.put(fieldName, errorMensage);
	}
}

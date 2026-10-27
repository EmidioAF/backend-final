package br.pucpr.restaurante.config

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ElementoNaoEncontradoException::class)
    fun handleNotFound(ex: ElementoNaoEncontradoException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Não encontrado")
    }

    @ExceptionHandler(RegraDeNegocioException::class)
    fun handleRegraDeNegocio(ex: RegraDeNegocioException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message ?: "Erro de negócio")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Erro de validação nos campos")
        val erros = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Valor inválido") }
        problem.setProperty("erros", erros)
        return problem
    }
}

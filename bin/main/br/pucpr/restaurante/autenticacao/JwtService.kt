package br.pucpr.restaurante.autenticacao

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class JwtService(@Value("\${api.security.token.secret}") private val secret: String) {

    fun gerarToken(email: String): String = JWT.create()
        .withIssuer("restaurante-api")
        .withSubject(email)
        .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
        .sign(Algorithm.HMAC256(secret))

    fun validarToken(token: String): String? = try {
        JWT.require(Algorithm.HMAC256(secret))
            .withIssuer("restaurante-api")
            .build()
            .verify(token)
            .subject
    } catch (e: Exception) {
        null
    }
}

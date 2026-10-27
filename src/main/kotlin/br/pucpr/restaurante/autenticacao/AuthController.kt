package br.pucpr.restaurante.autenticacao

import br.pucpr.restaurante.config.ElementoNaoEncontradoException
import br.pucpr.restaurante.usuarios.UsuarioRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

data class LoginDTO(val email: String, val senha: String)
data class TokenDTO(val token: String)

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginDTO): TokenDTO {
        val user = usuarioRepository.findByEmail(dto.email) ?: throw ElementoNaoEncontradoException("Usuário não existe")
        if (!passwordEncoder.matches(dto.senha, user.senha)) throw ElementoNaoEncontradoException("Senha incorreta")
        return TokenDTO(jwtService.gerarToken(user.email))
    }
}

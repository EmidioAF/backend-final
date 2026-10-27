package br.pucpr.restaurante.autenticacao

import br.pucpr.restaurante.usuarios.UsuarioRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter(
    private val jwtService: JwtService,
    private val usuarioRepository: UsuarioRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val token = req.getHeader("Authorization")?.replace("Bearer ", "")
        if (token != null) {
            val email = jwtService.validarToken(token)
            if (email != null) {
                val user = usuarioRepository.findByEmail(email)
                if (user != null) {
                    val auth = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }
        }
        chain.doFilter(req, res)
    }
}

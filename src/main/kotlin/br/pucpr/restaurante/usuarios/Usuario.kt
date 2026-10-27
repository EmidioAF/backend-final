package br.pucpr.restaurante.usuarios

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "usuarios")
class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    var nome: String = "",
    
    @Column(unique = true, nullable = false)
    var email: String = "",
    
    var senha: String = "",
    
    var role: String = "USER",

    @OneToOne(mappedBy = "usuario", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var perfil: PerfilCliente? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_$role"))
    override fun getPassword(): String = senha
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

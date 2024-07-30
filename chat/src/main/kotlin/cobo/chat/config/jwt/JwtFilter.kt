package cobo.chat.config.jwt

import cobo.chat.data.enum.RoleEnum
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            val studentId = jwtTokenProvider.getStudentId(token)
            if(jwtTokenProvider.isAccessToken(token) && studentId != null) {
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(studentId, null, listOf(SimpleGrantedAuthority((jwtTokenProvider.getRole(token)?: RoleEnum.STUDENT).name)))
            }
        }

        filterChain.doFilter(request, response)
    }
}
package cobo.writing.config

import jakarta.servlet.*
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import java.io.IOException

@WebFilter(urlPatterns = ["/**"])
class LogFilter: Filter {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest

        logger.info("Incoming request: {} {} by user ID {}",
            httpRequest.method, httpRequest.requestURI, SecurityContextHolder.getContext().authentication?.principal)

        chain.doFilter(request, response)
    }
}
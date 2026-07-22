package com.gpn.leads.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Runs ahead of the Spring Security filter chain (see {@link Ordered#HIGHEST_PRECEDENCE})
 * so requests are logged even when security rejects them with a 401/403.
 *
 * <p>Wraps the request/response in content-caching wrappers so the body can be read for
 * logging and still be available to the actual handler - a plain stream can only be
 * consumed once. This means the request body is only known once the handler has finished
 * reading it, so both the request and response are logged together after the chain runs,
 * not on arrival.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_LENGTH = 2000;
    private static final int REQUEST_CACHE_LIMIT_BYTES = 8192;
    private static final Pattern SENSITIVE_JSON_FIELD =
            Pattern.compile("(\"password\"\\s*:\\s*\")[^\"]*(\")", Pattern.CASE_INSENSITIVE);

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain) throws ServletException, IOException {

        final ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request, REQUEST_CACHE_LIMIT_BYTES);
        final ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        final String uri = requestUri(request);

        final long startedAt = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            final long durationMs = System.currentTimeMillis() - startedAt;

            log.info("--> {} {} from {} body: {}",
                    request.getMethod(),
                    uri,
                    request.getRemoteAddr(),
                    bodyOf(wrappedRequest.getContentAsByteArray(), request.getContentType()));

            log.info("<-- {} {} {} ({} ms) body: {}",
                    request.getMethod(),
                    uri,
                    wrappedResponse.getStatus(),
                    durationMs,
                    bodyOf(wrappedResponse.getContentAsByteArray(), wrappedResponse.getContentType()));

            // The caching wrapper buffers the body instead of writing it straight through,
            // so it has to be copied to the real response here or the client gets nothing back.
            wrappedResponse.copyBodyToResponse();
        }
    }

    private String requestUri(final HttpServletRequest request) {
        final String query = request.getQueryString();
        return query != null ? request.getRequestURI() + "?" + query : request.getRequestURI();
    }

    private String bodyOf(final byte[] content, final String contentType) {
        if (content.length == 0) {
            return "";
        }
        if (contentType != null && !isLoggableText(contentType)) {
            return "<binary %d bytes, %s>".formatted(content.length, contentType);
        }

        // Servlet API's getCharacterEncoding() defaults to ISO-8859-1 when a response doesn't
        // declare a charset, which mangles non-ASCII JSON bodies that are actually UTF-8 -
        // read the charset from the Content-Type header ourselves and fall back to UTF-8.
        final Charset charset = charsetOf(contentType);
        final int length = Math.min(content.length, MAX_BODY_LENGTH);
        final String body = SENSITIVE_JSON_FIELD
                .matcher(new String(content, 0, length, charset))
                .replaceAll("$1***$2");

        return content.length > MAX_BODY_LENGTH ? body + "...(truncated)" : body;
    }

    private boolean isLoggableText(final String contentType) {
        return contentType.startsWith("application/json")
                || contentType.startsWith("text/")
                || contentType.startsWith("application/xml");
    }

    private Charset charsetOf(final String contentType) {
        if (contentType == null) {
            return StandardCharsets.UTF_8;
        }
        final int charsetIndex = contentType.toLowerCase().indexOf("charset=");
        if (charsetIndex == -1) {
            return StandardCharsets.UTF_8;
        }
        try {
            return Charset.forName(contentType.substring(charsetIndex + "charset=".length()).trim());
        } catch (final RuntimeException e) {
            return StandardCharsets.UTF_8;
        }
    }
}

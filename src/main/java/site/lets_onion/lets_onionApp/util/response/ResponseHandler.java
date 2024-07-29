package site.lets_onion.lets_onionApp.util.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
@Component
public class ResponseHandler implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof ResponseDTO<?> responseDTO) {
      HttpStatus status = HttpStatus.valueOf(responseDTO.getCode());
      response.setStatusCode(status);
      return responseDTO;
    }
    return body;
  }
}

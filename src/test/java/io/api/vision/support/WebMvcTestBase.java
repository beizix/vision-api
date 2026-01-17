package io.api.vision.support;

import io.api.vision.useCases.auth.application.JwtUseCase;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc // 시큐리티 필터 적용 (기본값 addFilters = true)
public abstract class WebMvcTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected JwtUseCase jwtUseCase;

    protected String json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}

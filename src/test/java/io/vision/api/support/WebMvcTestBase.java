package io.vision.api.support;

import io.vision.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc // 시큐리티 필터 적용 (기본값 addFilters = true)
@ActiveProfiles("test")
public abstract class WebMvcTestBase {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @MockitoBean protected ManageAuthTokenUseCase manageAuthTokenUseCase;

  protected String json(Object obj) throws Exception {
    return objectMapper.writeValueAsString(obj);
  }
}
